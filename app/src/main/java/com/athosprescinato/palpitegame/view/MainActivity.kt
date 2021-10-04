package com.athosprescinato.palpitegame.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.athosprescinato.palpitegame.R
import com.athosprescinato.palpitegame.repository.ValorRepository
import com.athosprescinato.palpitegame.viewmodel.MainViewModel
import com.athosprescinato.palpitegame.viewmodelfactory.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: MainViewModel
    private var valorGerado: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Qual é o número?"

        setListeners()

    }

    override fun onClick(v: View) {
        if (v.id == R.id.button_novo_jogo) {
            novaPartida()
        } else if (v.id == R.id.button_enviar) {
            arriscarPalpite()
        }
    }

    // Inicializa os eventos de clickl

    private fun setListeners() {
        button_novo_jogo.setOnClickListener(this)
        button_enviar.setOnClickListener(this)
    }

    private fun novaPartida() {

        // Reconfigura para o estado inicial dos TextViews
        //imageView_numero_palpitado.setImageResource(R.drawable.ic_led_0)
        textView_dica.text = ""

        // Recebe da API o valor gerado
        val repository = ValorRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getValor()

        viewModel.myResponse.observe(this, { response ->
            /* Verifica se o retorno da API foi um sucesso
                caso seja sucesso, habilita o botão enviar e torna invisivel o botao de nova partida
                caso tenha falhado, exibi a mensagem erro e mostra o número do erro
            * */
            if (response.isSuccessful) {
                valorGerado = response.body()?.value!!
                button_enviar.isEnabled = true
                button_novo_jogo.visibility = View.GONE
            } else {
                textView_dica.text = resources.getString(R.string.erro)
                //imageView_numero_palpitado.text = response.code().toString()
            }
        })
    }

    private fun arriscarPalpite() {

        // Recebe a entrada do usuario e converte para String
        val palpiteUsuario = editText_palpite_usuario.text.toString()

        // Exibe a entrada do usuario no painel de palpite
        atualizarLed(palpiteUsuario)


        /*Converte a entrada do usuario para um inteiro e verifica se é um numero
           já faz as verificações entre a entrada do usuario e o valor gerado da API
           E retorna com as dicas ou se acertou.
        * */
        try {
            val palpiteInt = palpiteUsuario.toInt()

            if (palpiteInt == valorGerado) {
                textView_dica.text = resources.getString(R.string.acertou)
                button_novo_jogo.visibility = View.VISIBLE
                button_enviar.isEnabled = false

            } else if (palpiteInt < valorGerado) {
                textView_dica.text = resources.getString(R.string.maior)

            } else {
                textView_dica.text = resources.getString(R.string.menor)

            }

        } catch (nfe: NumberFormatException) {
            // Caso o valor de entrada não seja um valor valido, um Toast irá surgir avisando
            Toast.makeText(this, "Entre apenas com números inteiros", Toast.LENGTH_SHORT).show()

        }

        // A cada novo envio de palpite, o campo de entrada é limpo.
        editText_palpite_usuario.text?.clear()


    }

    private fun atualizarLed(palpite: String) {

        val tamanhoPalpite = palpite.length

        if (tamanhoPalpite == 1) {

            imageView_DisplayLed_3_1.visibility = View.GONE
            imageView_DisplayLed_3_2.visibility = View.GONE
            imageView_DisplayLed_3_3.visibility = View.GONE

            imageView_DisplayLed_2_1.visibility = View.GONE
            imageView_DisplayLed_2_2.visibility = View.GONE

            imageView_DisplayLed_1_1.visibility = View.VISIBLE

        } else if (tamanhoPalpite == 2) {

            imageView_DisplayLed_1_1.visibility = View.INVISIBLE

            imageView_DisplayLed_3_1.visibility = View.GONE
            imageView_DisplayLed_3_2.visibility = View.GONE
            imageView_DisplayLed_3_3.visibility = View.GONE

            imageView_DisplayLed_2_1.visibility = View.VISIBLE
            imageView_DisplayLed_2_2.visibility = View.VISIBLE

        } else if (tamanhoPalpite == 3){
            imageView_DisplayLed_1_1.visibility = View.INVISIBLE

            imageView_DisplayLed_2_1.visibility = View.GONE
            imageView_DisplayLed_2_2.visibility = View.GONE

            imageView_DisplayLed_3_1.visibility = View.VISIBLE
            imageView_DisplayLed_3_2.visibility = View.VISIBLE
            imageView_DisplayLed_3_3.visibility = View.VISIBLE

        }


    }


}