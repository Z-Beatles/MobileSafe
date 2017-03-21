package cn.waynechu.mobilesafe.chapter03.test;

import java.util.List;
import java.util.Random;
import cn.waynechu.mobilesafe.chapter03.db.dao.BlackNumberDao;
import cn.waynechu.mobilesafe.chapter03.entity.BlackContactInfo;
import android.content.Context;
import android.test.AndroidTestCase;
import android.util.Log;

/**
 * ���������������ݲ�����
 * 
 * @author waynechu
 * 
 */
public class BlackNumberDaoTest extends AndroidTestCase {
    private Context context;

    // setUp()��������ÿ�����Է���ǰ���ã����ڻ�ȡϵͳ�����������ģ�
    @Override
    protected void setUp() throws Exception {
        context = getContext();
        super.setUp();
    }

    /**
     * ������ӵķ���
     * 
     * @throws Exception
     */
    public void testAdd() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        Random random = new Random(8979);
        for (long i = 0; i < 30; i++) {
            BlackContactInfo info = new BlackContactInfo();
            info.phoneNumber = 15500000000l + i + "";
            info.contactName = "zhangsan" + i;
            info.mode = random.nextInt(3) + 1;
            dao.add(info);
        }
    }

    /**
     * ����ɾ���ķ���
     * 
     * @throws Exception
     */
    public void testDelete() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        BlackContactInfo info = new BlackContactInfo();
        for (long i = 1; i < 30; i++) {
            info.phoneNumber = 15500000000l + i + "";
            dao.delete(info);
        }
    }

    /**
     * ������������Ŀ
     * 
     * @throws Exception
     */
    public void testGetTotalNumber() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        int total = dao.getTotalNumber();
        Log.i("TestBlackNumberDao", "��������Ŀ��  " + total);
    }

    /**
     * ���Է�ҳ��ѯ
     * 
     * @throws Exception
     */
    public void testGetPageBlackNumber() throws Exception {
        BlackNumberDao dao = new BlackNumberDao(context);
        List<BlackContactInfo> list = dao.getPageBlackNumber(2, 5);
        for (int i = 0; i < list.size(); i++) {
            Log.i("TestBlackNumberDao", list.get(i).phoneNumber);
        }
    }

    public void testGetBlackContactMode() throws Exception {
    }
}
