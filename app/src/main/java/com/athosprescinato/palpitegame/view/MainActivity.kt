package com.athosprescinato.palpitegame.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
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

    fun novaPartida(){

        // Reconfigura para o estado inicial dos TextViews
        textView_numero_palpitado.setText("0")
        textView_dica.setText("")

        // Recebe da API o valor gerado
        val repository = ValorRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.getValor()

        viewModel.myResponse.observe(this, Observer { response ->
            /* Verifica se o retorno da API foi um sucesso
                caso seja sucesso, habilita o botão enviar e torna invisivel o botao de nova partida
                caso tenha falhado, exibi a mensagem erro e mostra o número do erro
            * */
            if(response.isSuccessful) {
                valorGerado = response.body()?.value!!
                button_enviar.setEnabled(true)
                button_novo_jogo.visibility = View.GONE
            } else {
                textView_dica.setText("Error")
                textView_numero_palpitado.setText(response.code().toString())
            }
        })
    }

    fun arriscarPalpite(){

        // Recebe a entrada do usuario e converte para String
        val palpiteUsuario = editText_palpite_usuario.getText().toString()

        // Exibe a entrada do usuario no painel de palpite
        textView_numero_palpitado.setText(palpiteUsuario)


        /*Converte a entrada do usuario para um inteiro e verifica se é um numero
           já faz as verificações entre a entrada do usuario e o valor gerado da API
           E retorna com as dicas ou se acertou.
        * */
            try {
                val palpiteInt = palpiteUsuario.toInt()

                 if (palpiteInt == valorGerado) {
                    textView_dica.setText("Acertou!!")
                     button_novo_jogo.visibility = View.VISIBLE
                     button_enviar.setEnabled(false)

                } else if (palpiteInt < valorGerado ){
                     textView_dica.setText("É maior")

                 } else {
                     textView_dica.setText("É menor")

                 }

            } catch (nfe: NumberFormatException) {
                // Caso o valor de entrada não seja um valor valido, um Toast irá surgir avisando
                Toast.makeText(this, "Entre apenas com números inteiros", Toast.LENGTH_SHORT).show()
            }

        // A cada novo envio de palpite, o campo de entrada é limpo.
        editText_palpite_usuario.getText()?.clear()


    }
}