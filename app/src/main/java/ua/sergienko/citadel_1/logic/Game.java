package ua.sergienko.citadel_1.logic;

import java.lang.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.igor0ser.Character.Name;

public class Game {
	private static List<Player> mPlayerList = new ArrayList<Player>(); // ������
	private List<Player> mPlayerTemporarylList = new ArrayList<Player>(); // �������������� list ��� �������������� �������� ������� (�� ����� ���������� ������)
	private static ArrayDeque<District> mDistrictDeck = new ArrayDeque<>(); // ������ ���������
	private List<Character> characterDeck; // ������ ����������
	private Random mRandom = new Random();

	public Game(String userName) {
		//������ ����
		mDistrictDeck.addAll(DistrictDeck.districtDeck()); // ��������� ������ ���������
		mPlayerList.add(new Player("HARRY")); //��������� ���� � ������
		mPlayerList.add(new Player("RYAN"));
		mPlayerList.add(new Player("JAMIE"));
		mPlayerList.add(new Player("STEVE"));
		mPlayerList.add(new Player("BEN"));
		mPlayerList.get(mRandom.nextInt(5)).setmKing(true); // ������ - ��������

		for (Player player : mPlayerList) {
			for (int i = 0; i < 4; i++) {
				System.out.println(player.getmName() + " received "
						+ mDistrictDeck.peek());
				player.addCard(mDistrictDeck.poll()); // ������� ��������� �� 4 ����� ��������
			}
		}

	}

	public Character step1() {
		characterDeck = CharacterDeck.characterDeck(); // ����� ������ ���������� �����������
		Character out;
		do {
			out = characterDeck.get(mRandom.nextInt(characterDeck.size())); // ������ ��������� ����������� �������� ����
		} while (out.getmName().equals(Name.KING));
		characterDeck.remove(out);
		do {
			out = characterDeck.get(mRandom.nextInt(characterDeck.size())); // ������ ��������� ����������� �������� ����
		} while (out.getmName().equals(Name.KING));
		characterDeck.remove(out);
		return out; // ������ - �������� �����
	}

	public Player step2() { //����� ����������
		Player king = null;
		Iterator<Player> iterator = mPlayerList.iterator(); // ������ ����������� ������ �� ������
		while (iterator.hasNext()) {
			Player player = iterator.next();
			if (!player.ismKing()) {
				iterator.remove();
				mPlayerTemporarylList.add(player);
			} else {
				king = player;
				System.out.println("������ ����� ������ - " + king.getmName()
						+ ", ��������� ����� ���� �� �������!");
				break;
			}

		}
		mPlayerList.addAll(mPlayerTemporarylList);
		mPlayerTemporarylList.clear();

		for (Player p : mPlayerList) { //������ �������� ���������
			p.chooseCharacter(characterDeck);
		}
		return king;
	}

	public void step3() {
		ArrayList<Player> thisTurnList = new ArrayList<>();
		thisTurnList.addAll(mPlayerList);
		Collections.sort(thisTurnList);
		for (Player player : thisTurnList) {
			System.out.println("����� " + player);
			int whomToKill = -1;
			switch (player.getmCharacter().getmName()) {
			case ASSASIN:
				whomToKill = player.kill(); // ����-�� �������
				player.turn();
				break;
			case THIEF:
				if (player.ismAlive()) { //�������� �� ����� ����������� ������ � ������ ����
					player.robb(whomToKill); // ������ � ����-�� (����� ���� ���� ��� �����)
					player.turn();
				}
				break;
			case MAGICIAN:
				if (player.ismAlive()) {
					player.checkRobbed();
					player.turn();
					player.cardAbilities();
					player.wizardChangeCards();
				}
				break;
			case KING:
				player.coronation();
				if (player.ismAlive()) {
					player.checkRobbed();
					player.turn();
					player.cardAbilities();
				}
				break;
			case BISHOP:
				if (player.ismAlive()) {
					player.checkRobbed();
					player.turn();
					player.cardAbilities();
				}
				break;
			case MERCHANT:
				if (player.ismAlive()) {
					player.checkRobbed();
					player.turn();
					player.cardAbilities();
					player.takeMoney(1);
				}
				break;
			case ARCHITECT:
				if (player.ismAlive()) {
					player.checkRobbed();
					player.turn();
					player.cardAbilities();
					player.buildDistrict(); // ����� ������� �� 3-� ��������� �� ���
					player.buildDistrict();
					player.getmHand().add(mDistrictDeck.pop()); //����� ��� ����� ����� ����
					player.getmHand().add(mDistrictDeck.pop());
				}
				break;
			case WARLORD:
				if (player.ismAlive()) {
					player.checkRobbed();
					player.turn();
					player.cardAbilities();
					player.destroyDistrict();
				}
				break;
			}
			System.out.println("������� " + player);
			System.out.println("*********************************************");
		}
		System.out.println("------------- ���� ��������: "
				+ mDistrictDeck.size());
	}

	public boolean step4() {
		boolean district8 = false;
		for (Player player : mPlayerList) {
			if (player.getmTable().size() > 7) {
				player.setFirstBuild8Dustricts(true);
				district8 = true;
				break;
			}
		}
		if (district8) {
			System.out
					.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>���� ��������<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			int max = 0;
			Player winner = null;
			for (Player player : mPlayerList) {
				System.out.println(player);
				System.out.println(Arrays
						.toString(player.getmTable().toArray()));
				System.out.println(">>����: " + player.points());
				if (player.points() > max) {
					max = player.points();
					winner = player;
				}
			}
			System.out.println("������� - " + winner.getmName() + "!!!");
			return false;
		}

		System.out
				.println("����� ���� �� �������� 8-� �������. ���� ������������.");
		System.out
				.println("-------------------------------------------------------------------------------------------------------------------------------");
		return true;
	}

	public static ArrayDeque<District> getmDistrictDeck() {
		return mDistrictDeck;
	}

	public static List<Player> getmPlayerList() {
		return mPlayerList;
	}

}
