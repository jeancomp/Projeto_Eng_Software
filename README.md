# Projeto_Eng_Software
O projeto monitora dados dos sinais vitais dos pacientes



## 1 Descrição do Projeto

### 1.1 Escopo

Propomos desenvolvimento de um sistema de monitoramento e análise de coleta de dados de
monitores multiparâmetros de pacientes em UTIs, o aplicativo será capaz de capturar os dados de
monitores que mostram os sinais vitais do paciente(e. g., pressão arterial, frequência cardíaca,
frequência respiratória), sendo capaz de transmiti-los ao sistema móvel, os dados passam por uma
análise. Primeiramente, os dados são lidos do Dataset, interpretados e selecionados os sensores de
maior interesse dentre os diversos sensores que apresentam no Dataset, sendo depois transmitidos às
telas dos dispositivos. Depois que os dados são lidos e interpretados, dispara-se algum alerta se atender
a uma certa regra previamente definida, conforme mostrado na figura 1, essa regra é definida pelo
monitor da aplicação.

### 1.2 Requisitos

|                                             REQUISITOS FUNCIONAIS                                            |                                        REQUISITOS NÃO FUNCIONAIS                                        |
|:------------------------------------------------------------------------------------------------------------:|:-------------------------------------------------------------------------------------------------------:|
| RF01- O sistema deverá ter funcionalidade deautenticação para controle de acesso                             | RNF01 - O sistema terá suporte para plataforma Android                                                  |
| RF02 - O sistema deverá ter um dashboard para o usuário selecionar o paciente que será monitorado            | RNF02 - O aplicativo deverá ter interface intuitiva e fácil acesso as funcionalidades principais        |
| RF03 - O sistema deve oferecer função de ler arquivo do dataset, limpar ruídos editar                        | RNF03 - O sistema deve ser confiável, baixa taxa de falhas e não exigir muito processamento nem memória |
| RF04 - O sistema deverá gerar relatórios de leituras                                                         |                                                                                                         |
| RF05 - O sistema deve enviar notificações e alertas para o dispositivo móvel para acompanhamento do paciente |                                                                                                         |




### 1.3 Definição Tipo de Arquitetura

Nosso sistema utiliza o CDDL(Camada de Distribuição de Dados de Contexto) para transmitir as
mensagens para outros dispositivos móveis. O CDDL é um middleware publicador/subscritor que
fornece às aplicações clientes a capacidade de atuar como produtoras e consumidoras de dados de
contexto[1], ele adiciona metadados de qualidade de contexto às informações publicada.

Na figura 1 apresentamos a arquitetura do aplicativo, onde:

● Na coleta, realizamos a leitura dos dados no dataset, depois identificamos os dados mais
relevantes, por exemplo: temperatura, batimentos cardíacos

● Depois, realizamos o processamento dos dados, onde faremos o monitoramento dos dados
lido, caso identificamos piora no quadro do paciente, iremos disparar um alarme nos
dispositivos, esse alarme é construído no módulo de Regra EPL, onde no CDDL possui um
componente chamado de Monitor, nesse componente construímos a regra que é muito próxima
da linguagem SQL.

● Por fim, realizaremos a notificação nos dispositivos, utilizamos o CDDL, onde é responsável
pela transmissão dos dados para os dispositivos móveis



## Autores

[Projeto engenharia de software](https://github.com/jeancomp/Projeto_Eng_Software)

* **Jean Marques** 

* **João Pedro**   




