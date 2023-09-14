package componentes;

import javax.swing.*;
import javax.swing.text.MaskFormatter;

public class MeuCampoCPF extends MeuJFormattedTextField implements MeuComponente {

    public MeuCampoCPF(int colunas, boolean obrigatorio, String dica) {
        super(colunas, obrigatorio, dica);
        try {
            MaskFormatter mf = new MaskFormatter("###.###.###-##");
            mf.setPlaceholderCharacter(' ');
            mf.install(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Não foi possível criar o campo CPF!");
        }
    }

    @Override
    public void limpar() {
        setText("");
    }

    @Override
    public boolean eValido() {
        if (!eObrigatorio() && eVazio()) {
            return true;
        }
        String pega_valor = getText();
        if (!pega_valor.substring(0, 1).equals(" ")) {  // metodo subString (0,1) casa 0 , ou seja , começa a partir dessa casa e  que determina que ele pega a casa 1
            //JOptionPane.showMessageDialog(null, ""+pega_valor.substring(0,1));
            try {
                int d1, d2;
                int digito1, digito2, resto;
                int digitoCPF;
                String DigResultado;
                pega_valor = pega_valor.replace('.', ' ');
                // tira os pontos e poem espaço
                pega_valor = pega_valor.replace('-', ' ');
                pega_valor = pega_valor.replaceAll(" ", "");
                d1 = d2 = 0;

                digito1 = digito2 = resto = 0;

                //085564619
                for (int nCount = 1; nCount < pega_valor.length() - 1; nCount++) {
                    digitoCPF = Integer.valueOf(pega_valor.substring(nCount - 1, nCount)).intValue();
                    //0                                              //1 -1 = 0 , 1 (0,1) .. (1,2) .. (2,3)
                    //multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.     
                    d1 = d1 + (11 - nCount) * digitoCPF;
                    // 0 = 0 + (11 - 1) * 0 primeiro digito
                    //248

                    //para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.     
                    d2 = d2 + (12 - nCount) * digitoCPF;
                    //248
                }

                //Primeiro resto da divisão por 11.     
                resto = (d1 % 11);
                //248 / 11 = 22 = 242 >> 248-242 = 6 >> resto

                //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.     
                if (resto < 2) {
                    digito1 = 0;
                } else {
                    digito1 = 11 - resto;
                    //6 = 5  
                }

                // primeiro digito verificador multiplicado por 2, que depois é somado a d2
                d2 += 2 * digito1;
                // adiciona ao d2 o primeiro digito verificador (5)

                //Segundo resto da divisão por 11.     
                resto = (d2 % 11);
                //302  / 11 = 297 >> 302-297 = 5 >> resto

                //Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.     
                if (resto < 2) {
                    digito2 = 0;
                } else {
                    digito2 = 11 - resto;
                    // 5 = 6
                }

                //Digito verificador do CPF que está sendo validado.     
                String DigVerificador = pega_valor.substring(pega_valor.length() - 2, pega_valor.length());
                // 11-2 = 9 , a partir da casa 10 ,ou seja , pega o que sobrou , que no caso foi 56
                //JOptionPane.showMessageDialog(null, ""+DigVerificador);

                //Concatenando o primeiro resto com o segundo.     
                DigResultado = String.valueOf(digito1) + String.valueOf(digito2);
                //5                         //6

                //comparar o digito verificador do cpf com o primeiro resto + o segundo resto.     
                return DigVerificador.equals(DigResultado);
                //56                    //56 retorna true ; o CPF é válido
            } catch (Exception e) {
                System.err.println("Erro !" + e);
                return false;
            }
        } else {
            return false;

        }

    }

    @Override
    public boolean eVazio() {

        return getText().replace("-", "").replace("/", "").replace(".", "").replace("_", "").trim().isEmpty();

    }

}


/*O primeiro dígito verificador do CPF é calculado utilizando-se o seguinte algoritmo. 

 1) Distribua os 9 primeiros dígitos em um quadro colocando os pesos 10, 9, 8, 7, 6, 5, 4, 3, 2 abaixo da esquerda para a direita, conforme representação abaixo:

 0	8	5	5	6	4	6	1	9
 10	9	8	7	6	5	4	3	2
 2) Multiplique os valores de cada coluna:

 0	8	5	5	6	4	6	1	9
 10	9	8	7	6	5	4	3	2
 0	72	40	35	36	20	24	3	18
 3) Calcule o somatório dos resultados = 248

 4) O resultado obtido (248) será divido por 11. Considere como quociente apenas o valor inteiro, o resto da divisão será responsável pelo cálculo do primeiro dígito verificador.

 Vamos acompanhar: 248 dividido por 11 obtemos 22 como quociente e 6 como resto da divisão. Caso o resto da divisão seja menor que 2, o nosso primeiro dígito verificador se torna 
 * 0 (zero), caso contrário subtrai-se o valor obtido de 11, que é nosso caso. Sendo assim nosso dígito verificador é 11-6, ou seja, 5 (cinco). Já temos portanto parte do CPF,
 * confira: 085.564.619-5X.
 Calculando o Segundo Dígito Verificador

 1) Para o cálculo do segundo dígito será usado o primeiro dígito verificador já calculado. Montaremos uma tabela semelhante a anterior só que desta vez usaremos na segunda linha 
 * os valores 11,10,9,8,7,6,5,4,3,2 já que estamos incorporando mais um algarismo para esse cálculo. Veja:

 0	8	5	5	6	4	6	1	9       5
 11	10	9	8	7	6	5	4	3	2
 2) Na próxima etapa faremos como na situação do cálculo do primeiro dígito verificador, multiplicaremos os valores de cada coluna e efetuaremos o somatório dos resultados obtidos:
 * (11+10+...+21+6) = 204. 

 0	8	5	5	6	4	6	1	9       5
 11      10	9	8	7	6	5	4	3	2       
 0	80	45	40	42	24	30	4	27      10
 3) Realizamos novamente o cálculo do módulo 11. Dividimos o total do somatório por 11 e consideramos o resto da divisão.

 Vamos acompanhar: 302 dividido por 11 obtemos 26 como quociente e 5 como resto da divisão.

 4) Caso o valor do resto da divisão seja menor que 2, esse valor passa automaticamente a ser zero, caso contrário (como no nosso caso) é necessário subtrair o valor obtido de 11
 * para se obter o dígito verificador. Logo, 11-5= 6, que é o nosso segundo dígito verificador.

 Neste caso chegamos ao final dos cálculos e descobrimos que os dígitos verificadores do nosso CPF hipotético são os números 5 e 6, portanto o CPF ficaria assim: 085.564.619.56.

 O gerador de CPF apresentado neste site funciona com base neste algoritmo. A rotina de gerar CPF 's válidos, inicialmente sorteia 9 números. Calcula-se o 1o dígito verificador e
 * integra-se o mesmo aos 9 números iniciais. Prossegue-se com o cálculo do segundo dígito verificador como ensinado. Ao final, o criador de CPF emite um número de CPF válido.*/
