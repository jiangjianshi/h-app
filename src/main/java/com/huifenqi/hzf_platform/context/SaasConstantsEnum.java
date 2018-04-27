package com.huifenqi.hzf_platform.context;

import java.util.HashMap;

import com.huifenqi.hzf_platform.utils.SaasHouseCheckUtil;

/**
 * @Description:
 * @Author changmingwei
 * @Date 2017/6/29 0014 12:01
 */
public class SaasConstantsEnum {

    public interface EnumValue {
        String getName();

        int getIndex();
    }

	/**
	 * 同步状态
	 */
	public enum IsTopEnum implements EnumValue {

		TOP(1, "置顶"), NOTTIP(0, "取消置顶");

		private final Integer index;
        private final String name;

		IsTopEnum(int index, String name) {
            this.index = index;
            this.name = name;
        }

		private static HashMap<Integer, String> map = new HashMap<Integer, String>();

        static {
			for (IsTopEnum item : IsTopEnum.values()) {
                map.put(item.getIndex(), item.getName());
            }
        }

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public int getIndex() {
			return this.index;
		}

		public static boolean checkIndexExist(int index, boolean isValue) {
			if (!isValue) {
				if (index == SaasHouseCheckUtil.DEFAULT_ENUM_INT) {
					return true;
				}
			}

			if (map.containsKey(index)) {
				return true;
			}
			return false;
		}

		public static String getName(Integer index) {
			if (index == null)
				return null;
			return map.get(index);
		}
	}
   
}
