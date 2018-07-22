package cn.self.cloud.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduleFixed  {
	public final static long COPYROLE = 1000 * 60 * 1;

	@Scheduled(fixedDelay = ScheduleFixed.COPYROLE)
	public void aa(){
	}

}
