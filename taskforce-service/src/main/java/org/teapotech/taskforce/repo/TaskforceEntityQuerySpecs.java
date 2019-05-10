/**
 * 
 */
package org.teapotech.taskforce.repo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.teapotech.taskforce.entity.TaskforceEntity;
import org.teapotech.taskforce.entity.TaskforceGroup;

/**
 * @author jiangl
 *
 */
public class TaskforceEntityQuerySpecs {

	public static Specification<TaskforceEntity> queryTaskforceEntity(String id, String name,
			TaskforceGroup group) {
		return new Specification<TaskforceEntity>() {
			private static final long serialVersionUID = 418739538937855928L;

			@Override
			public Predicate toPredicate(Root<TaskforceEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				if (StringUtils.isNotBlank(id)) {
					predicates.add(cb.equal(root.<String>get("id"), id));
				}

				if (StringUtils.isNotBlank(name)) {
					predicates.add(cb.like(root.<String>get("name"), "%" + name + "%"));
				}

				if (group != null) {
					predicates.add(cb.equal(root.<TaskforceGroup>get("group"), group));
				}

				return cb.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

}
