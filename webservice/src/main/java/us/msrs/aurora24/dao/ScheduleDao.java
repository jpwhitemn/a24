package us.msrs.aurora24.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import us.msrs.aurora24.exception.NotFoundByID;
import us.msrs.aurora24.model.Schedule;

@Repository
public class ScheduleDao {

	private static List<Schedule> schedules;

	public List<Schedule> getSchedules() {
		if (schedules == null) {
			schedules = new ArrayList<Schedule>();
			schedules.addAll(Arrays.asList(
					new Schedule("everyNight", UUID.randomUUID(), "Jim", true, "0 0 1 * * ?"),
					new Schedule("everyMonNoon", UUID.randomUUID(), "Jim", true, "0 0 12 ? * MON"),
					new Schedule("onceYear", UUID.randomUUID(), "Jim", true, "0 0 21 31 DEC ? *")));
		}
		return schedules;
	}

	public Schedule getByID(String id) {
		return getSchedules().stream()
				.filter(schedule -> schedule.id().equals(UUID.fromString(id)))
				.findFirst()
				.orElse(null);
	}

	public Schedule getByName(String name) {
		return getSchedules().stream()
				.filter(schedule -> schedule.name().contentEquals(name)).findFirst().orElse(null);
	}

	public Schedule createSchedule(String name, String creator, boolean active, String cron) {
		Schedule schedule = new Schedule(name, UUID.randomUUID(), creator, active, cron);
		if (schedules == null) {
			schedules = Arrays.asList(schedule);
		} else {
			schedules.add(schedule);
		}
		return schedule;
	}

	public boolean deleteSchedule(String id) {
		Schedule schedule = getByID(id);
		if (schedule != null) {
			schedules.remove(schedule);
			return true;
		}
		return false;
	}

	public Schedule updateSchedule(String id, String name, String creator, boolean active, String cron)
			throws NotFoundByID {
		Schedule schedule = getByID(id);
		if (schedule != null) {
			Schedule updatedSchedule = new Schedule(name, UUID.fromString(id), creator, active, cron);
			Collections.replaceAll(schedules, schedule, updatedSchedule);
			return updatedSchedule;
		}
		throw new NotFoundByID("Schedule not found with id: " + id);
	}

}
