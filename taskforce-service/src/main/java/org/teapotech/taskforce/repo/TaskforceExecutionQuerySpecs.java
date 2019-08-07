/**
 * 
 */
package org.teapotech.taskforce.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.teapotech.taskforce.entity.TaskforceExecution;
import org.teapotech.taskforce.entity.TaskforceExecution.Status;

/**
 * @author jiangl
 *
 */
public class TaskforceExecutionQuerySpecs {

	public static Specification<TaskforceExecution> queryTaskforceExecution(String id, String taskforceId,
			Collection<Status> status, Date startTime, String startBy) {
		return new Specification<TaskforceExecution>() {

			private static final long serialVersionUID = -978347818192910918L;

			@Override
			public Predicate toPredicate(Root<TaskforceExecution> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				if (StringUtils.isNotBlank(id)) {
					predicates.add(cb.equal(root.<String>get("id"), id));
				}

				if (StringUtils.isNotBlank(taskforceId)) {
					predicates.add(cb.equal(root.<String>get("taskforceId"), taskforceId));
				}

				if (status != null && !status.isEmpty()) {
					predicates.add(root.<List<Status>>get("status").in(status));
				}

				if (startBy != null) {
					predicates.add(cb.equal(root.<String>get("startBy"), id));
				}

				if (startTime != null) {
					Date sd = startTime;
					Date ed = new Date();
					predicates.add(cb.and(cb.greaterThanOrEqualTo(root.<Date>get("startTime"), sd),
							cb.lessThan(root.<Date>get("startTime"), ed)));
				}

				return cb.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

}
