# Organizze Clone

#### Projeto feito com base no curso <a href="https://www.udemy.com/course/curso-de-desenvolvimento-android-oreo/"> Desenvolvimento Android Completo 2021 - Crie 18 Apps </a>

#### Foram utilizadas a tecnologia JAVA e o Servidor e Banco de Dados Firebase

### Objetivo

#### Ajudar o usuário no controle de seus gastos e ganhos, podendo verificar o saldo total, adicionar novos gastos ou ganhos, sendo mais focado para o público jovem, que não possuem ganho de renda fixa, como salário ou mesada.

## Projeto

#### Utilizando a biblioteca <a href="https://github.com/ogaclejapan/SmartTabLayout"> SmartTabLayout </a> para a criação de abas para alar um pouco sobre o App

<img heigth="500" src="https://github.com/SouzaGiovanna/CursoAndroid/blob/master/README/README_Organizze_1.gif">

#### O cadastro do Usuário é realizado utilizando o Firebase Autentication, além de cadastrar no REALTIME DATABASE do próprio Firebase, caso ocorra algum erro durante a criação da conta, como email já cadastrado, entre outros, serão apresentados em forma de Toast para o usuário.

<img heigth="500" src="https://github.com/SouzaGiovanna/CursoAndroid/blob/master/README/README_Organizze_2.gif">

#### Após o cadastro, o usuário é encaminhado para a tela inicial do app, onde ele tem acesso às despesas e ganhos, podendo navegar pelos meses, além de ter o saldo total sendo apresentado a todo momento. Foi utilizado um Floating Action Button para mostrar as opções de adicionar despesas ou receitas, que quando pressionadas abrem a tela correspondente, assim podendo fazer o registro. Quando finalizado, o usuário é redirecionado para a tela inicial, onde o saldo total é atualizado automaticamente e é mostrado a movimentação registrada, tendo o valor destacado em verde para receitas e em vermelho para despesas.

<img heigth="500" src="https://github.com/SouzaGiovanna/CursoAndroid/blob/master/README/README_Organizze_3.gif">

#### Quando uma nova receita ou despesa vai ser salva, o aplicativo já carrega a data em que a operação está sendo realizada, mas o usuário tem a opção de mudar essa data, podendo colocar a data de algum evento passado ou futuro, como mostrado no exemplo abaixo, e assim q essa movimentação for salva, poderá ser consultado no mês em que foi salva a mesma, nesse caso em janeiro de 2021. Para poder organizer melhor as movimentações está sendo utilizada a biblioteca <a href="https://github.com/prolificinteractive/material-calendarview"> Calendar View </a>

<img heigth="500" src="https://github.com/SouzaGiovanna/CursoAndroid/blob/master/README/README_Organizze_4.gif">

#### Por fim, caso o usuário tenha salvo alguma movimentação errada, ele poderá arrastá-la para a lateral da tela, onde será exibido uma Alert Dialog, para que seja confirmada a deleção, e caso o usuário confirme, a movimentação vai ser deletada e o saldo atual será atualizado automaticamente

<img heigth="500" src="https://github.com/SouzaGiovanna/CursoAndroid/blob/master/README/README_Organizze_5.gif">
