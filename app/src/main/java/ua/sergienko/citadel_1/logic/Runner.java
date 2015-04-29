package ua.sergienko.citadel_1.logic;

public class Runner {
	public static void main(String[] args) {

		Game game = new Game("Igor");
		int i = 1;
		do {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>> ��� #" + i + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			i++;
			System.out.println("��� 1");
			System.out.println("�������� ����� �����������: " + game.step1());
			System.out.println("��� 2");
			game.step2();
			System.out.println("��� 3");
			game.step3();
			System.out.println("��� 4");
			} while (game.step4());

	}
}
