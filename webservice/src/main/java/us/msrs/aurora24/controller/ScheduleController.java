package us.msrs.aurora24.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import us.msrs.aurora24.dao.ScheduleDao;
import us.msrs.aurora24.exception.NotFoundByID;
import us.msrs.aurora24.model.Schedule;

@Controller
public class ScheduleController {

	@Autowired
	ScheduleDao dao;

	@QueryMapping
	public List<Schedule> schedules() {
		return dao.getSchedules();
	}

	@QueryMapping
	public Schedule scheduleById(@Argument String id) {
		return dao.getByID(id);
	}

	@QueryMapping
	public Schedule scheduleByName(@Argument String name) {
		return dao.getByName(name);
	}

	@MutationMapping
	public Schedule createSchedule(@Argument String name, @Argument String creator, @Argument boolean active,
			@Argument String cron) {
		return dao.createSchedule(name, creator, active, cron);
	}

	@MutationMapping
	public boolean deleteSchedule(@Argument String id) {
		return dao.deleteSchedule(id);
	}
	
	@MutationMapping
	public Schedule updateSchedule(@Argument String id, @Argument String name, @Argument String creator, @Argument boolean active,
			@Argument String cron) throws NotFoundByID {
		return dao.updateSchedule(id, name, creator, active, cron);
	}
}
