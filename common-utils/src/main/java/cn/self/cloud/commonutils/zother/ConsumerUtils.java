package cn.self.cloud.commonutils.zother;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by cp on 2017/6/22.
 */
public class ConsumerUtils {
    public static class IntegerConsumer implements Consumer<ResultSet> {
        private Integer integer;

        @Override
        public void accept(ResultSet resultSet) {
            try {
                if (resultSet.next()) {
                    integer = resultSet.getInt(1);
                } else {
                    integer = 0;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public int getResult() {
            return integer;
        }
    }

    public static class StringConsumer implements Consumer<ResultSet> {
        private String string;

        @Override
        public void accept(ResultSet resultSet) {
            try {
                if (resultSet.next()) {
                    string = resultSet.getString(1);
                } else {
                    string = "";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public String getResult() {
            return string;
        }
    }

    public static class ListStringConsumer implements Consumer<ResultSet> {
        private List<String> list = new ArrayList<>();

        @Override
        public void accept(ResultSet resultSet) {
            try {
                list.clear();
                while (resultSet.next()) {
                    list.add(resultSet.getString(1));
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        public List<String> getList() {
            return list;
        }
    }

    public static IntegerConsumer getIntegerConsumer() {
        return new IntegerConsumer();
    }

    public static StringConsumer getStringConsumer() {
        return new StringConsumer();
    }
}
