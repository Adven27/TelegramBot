package org.telegram.mamot.services;

import java.util.*;

public class DAO {

    public String getEndWord(int index) {
        return endWords().get(index % endWords().size());
    }

    private static List<String> endWords() {
        ArrayList words = new ArrayList();
        words.add("��������");
        words.add("����� ����");
        words.add("��� �������");
        words.add("������!");
        return words;
    }

    public Map<Integer, String> getWeekMenu() {
        Map<Integer, String> words = new HashMap();
        words.put(1, "30% �� ������ � 2 �������� �� ���� ������");
        words.put(2, "30% �� ����� � 2 �������� �� ���� ������");
        words.put(3, "30% �� ����� � ������� � 2 �������� �� ���� ������");
        words.put(4, "30% �� ������� ������� � 2 �������� �� ���� ������");
        words.put(5, "� 3 ����� ���� ������ �� ��� ����������� �������� 30%");
        words.put(6, "� 3 ����� ���� ������ �� ��� ����������� �������� 30%");
        words.put(7, "30% �� ��� ������� ����� � 2 �������� �� ���� ������");
        return words;
    }

    public String getComplement() {
        Map<Integer, String> words1 = new HashMap();
        words1.put(0, "���");
        words1.put(1, "�����");
        words1.put(2, "���������");
        words1.put(3, "������");

        Map<Integer, String> words2 = new HashMap();
        words2.put(0, "�����");
        words2.put(1, "��������");
        words2.put(2, "������");
        words2.put(3, "������");

        Map<Integer, String> words3 = new HashMap();
        words3.put(0, "���������");
        words3.put(1, "�������");
        words3.put(2, "������");
        words3.put(3, "�����������");
        words3.put(4, "������� ��� ��� ���������");

        Map<Integer, String> words4 = new HashMap();
        words4.put(0, "�����");
        words4.put(1, "�������");
        words4.put(2, "������");
        words4.put(3, "������");

        Random random = new Random();
        return "�� " + words1.get(random.nextInt(4)) + " " +
               words2.get(random.nextInt(4)) + " " +
               words3.get(random.nextInt(5)) + " " +
               words4.get(random.nextInt(4));
    }
}