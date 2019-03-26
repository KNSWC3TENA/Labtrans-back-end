# Labtrans-back-end
Tecnologias Utilizadas:
  Java e suas ferramentas, em breve o banco de dados SQLite.
 
23/03 - Iniciado projeto, criadas classes Pagina, Controlador e Calendario com funções básicas
24/03 - Classe Calendario descartada, funções de seleção de dia e hora agora são feitas pela package calendarioBeans
        Adicionada mais funções às classes Controlador e Pagina
        Criada classe Filiais - responsável por trabalhar juntamente das listas de edição e cadastramento de reservas
        Corrigidos diversos erros no código
25/03   Classe Controlador agora é responsável de processar as variáveis recebidas das páginas de movimentação de dados, atualmente contém          algumas da página de cadastros. Em breve será utilizada para a movimentação de dados no banco.
        Corrigidos erros de atribuição de beans nas classes
        Classe filiais agora completa, responsável por manter os dados de filiais e locais de reuniões.
        Classe Pagina será responsável pelas mensagens do sistema à serem mostradas ao usuário
26/03 - Classe filiais teve suas funções transferidas para a classe Controlador.
        Classe Lista criada, responsável pela listagem da página de listar, recebeu a função de SELECT da classe Controlador
        Classe Lista também é responsável pela construção dos valores da dataTable da página Listar, assim como em breve o encaminhamento
          das variáveis para exclusão e edição na classe Controlador.
        Diversos Ajustes no Controlador
