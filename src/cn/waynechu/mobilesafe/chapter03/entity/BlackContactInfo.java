package cn.waynechu.mobilesafe.chapter03.entity;

/**
 * ��ϵ����Ϣ��ʵ����
 * 
 * @author waynechu
 * 
 */
public class BlackContactInfo {
    /** ���������� */
    public String phoneNumber;
    /** ��������ϵ������ */
    public String contactName;
    /**
     * ����������ģʽ 1-�绰���� 2-�������� 3-�绰�����Ŷ�����
     */
    public int mode;

    public String getModeString(int mode) {
        switch (mode) {
        case 1:
            return "�绰����";
        case 2:
            return "��������";
        case 3:
            return "�绰����������";
        }
        return "";
    }
}
