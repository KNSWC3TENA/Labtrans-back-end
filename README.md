# Labtrans-back-end
Tecnologias Utilizadas:
  Java
  SQLite
 
 Dificuldades Encontradas:
  Construção de API RESTful descartada por não ser considerada um requisito funcional e priorizada a construção para a manipulação de dados juntamente de JSF (JSF é naturalmente inviável para a construção REST, a escolha de framework teria de ser outra.)
  Injeção de dados e manipulação por objetos devido ao front-end em JSF foi inicialmente confuso de se configurar, porém por possuir afinidade com Java SE tais foram problemas temporários, apesar de a organização do código em geral (nome de classes e local das funções) não se encontrar em perfeito estado caso futuramente seja necessário elas podem ser modificadas e no momento se encontram em funcionamento pleno.
 
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
27/03 - Adicionadas funções para receber e atribuir os valores selecionados na página de listar.
        Adicionados encaminhamentos de edição e exclusão de dados da classe Listar para os itens selecionados (edição apenas considera o              primeiro, exclusão considera todos)
        Diversos pequenos ajustes.
