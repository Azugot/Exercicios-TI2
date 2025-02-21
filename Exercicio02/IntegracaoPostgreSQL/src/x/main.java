package x;

import java.util.Scanner;

public class main {

	public static void main(String[] args) {

		DAO dao = new DAO();
		Scanner scanner = new Scanner(System.in);

		dao.conectar();
		System.out.println("Conectado ao servidor");

		int opcao = 0;

		while (opcao != 5) {
			System.out.println("1. Listar");
			System.out.println("2. Inserir");
			System.out.println("3. Excluir");
			System.out.println("4. Atualizar");
			System.out.println("5. Sair");
			System.out.print("Escolha uma opção: ");
			opcao = scanner.nextInt();

			switch (opcao) {
			case 1:
				X[] xs = dao.getX();
				if (xs != null) {
					for (X x : xs) {
						System.out.println("ID: " + x.getID() + ", Nome: " + x.getNome() + ", Idade: " + x.getIdade());
					}
				} else {
					System.out.println("Nenhum registro encontrado.");
				}
				break;
			case 2:
				System.out.print("Digite o ID: ");
				int idInserir = scanner.nextInt();
				scanner.nextLine(); // consumir o newline
				System.out.print("Digite o nome: ");
				String nomeInserir = scanner.nextLine();
				System.out.print("Digite a idade: ");
				int idadeInserir = scanner.nextInt();

				X xInserir = new X(idInserir, nomeInserir, idadeInserir);
				if (dao.inserirX(xInserir)) {
					System.out.println("Registro inserido com sucesso!");
				} else {
					System.out.println("Erro ao inserir o registro.");
				}
				break;
			case 3:
				System.out.print("Digite o ID do registro a ser excluído: ");
				int idExcluir = scanner.nextInt();

				if (dao.excluirX(idExcluir)) {
					System.out.println("Registro excluído com sucesso!");
				} else {
					System.out.println("Erro ao excluir o registro.");
				}
				break;
			case 4:
				System.out.print("Digite o ID do registro a ser atualizado: ");
				int idAtualizar = scanner.nextInt();
				scanner.nextLine(); // consumir o newline
				System.out.print("Digite o novo nome: ");
				String nomeAtualizar = scanner.nextLine();
				System.out.print("Digite a nova idade: ");
				int idadeAtualizar = scanner.nextInt();

				X xAtualizar = new X(idAtualizar, nomeAtualizar, idadeAtualizar);
				if (dao.atualizarX(xAtualizar)) {
					System.out.println("Registro atualizado com sucesso!");
				} else {
					System.out.println("Erro ao atualizar o registro.");
				}
				break;
			case 5:
				System.out.println("Saindo...");
				break;
			default:
				System.out.println("Opção inválida");
			}

		}
		dao.close();
		scanner.close();
	}
}