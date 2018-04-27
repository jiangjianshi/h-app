package com.huifenqi.hzf_platform.context.entity.phone;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;  
  
/**
 * 
 * @author jjs
 *
 */
public class Message implements Delayed{ 
	
        private String id;  
        private String secretNo;  
        private long activeTime;//执行时间     
          
        public Message(){  
              
        }  
          
        public Message(String id, String secretNo,long activeTime) {  
            super();  
            this.id = id;  
            this.secretNo = secretNo;  
            this.activeTime = TimeUnit.NANOSECONDS.convert(activeTime, TimeUnit.MILLISECONDS) + System.nanoTime();  
        }  
        
        public String getId() {  
            return id;  
        }  
        public void setId(String id) {  
            this.id = id;  
        }  

		public String getSecretNo() {
			return secretNo;
		}

		public void setSecretNo(String secretNo) {
			this.secretNo = secretNo;
		}

		public long getActiveTime() {
			return activeTime;
		}

		public void setActiveTime(long activeTime) {
			this.activeTime = activeTime;
		}

		@Override  
	public int compareTo(Delayed delayed) {
		Message msg = (Message) delayed;
		long d = getDelay(TimeUnit.NANOSECONDS) - msg.getDelay(TimeUnit.NANOSECONDS);
		return d == 0 ? 0 : (d < 0 ? -1 : 1);
	}  
  
        @Override  
        public long getDelay(TimeUnit unit) {  
             return unit.convert(this.activeTime - System.nanoTime(), TimeUnit.NANOSECONDS);   
        }  
          
}  