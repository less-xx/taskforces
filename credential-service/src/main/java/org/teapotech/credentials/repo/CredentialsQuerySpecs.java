/**
 * 
 */
package org.teapotech.credentials.repo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.teapotech.credentials.entity.Credentials;
import org.teapotech.credentials.entity.Credentials.CredentialType;

/**
 * @author jiangl
 *
 */
public class CredentialsQuerySpecs {

	public static Specification<Credentials> queryCredentials(String id, CredentialType type, Boolean enabled,
			Date lastUpdatedTime, String updatedBy) {
		return new Specification<Credentials>() {

			private static final long serialVersionUID = -978347818192910918L;

			@Override
			public Predicate toPredicate(Root<Credentials> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicates = new ArrayList<Predicate>();

				if (StringUtils.isNotBlank(id)) {
					predicates.add(cb.equal(root.<String>get("id"), id));
				}

				if (type != null) {
					predicates.add(cb.equal(root.<CredentialType>get("type"), type));
				}

				if (enabled != null) {
					predicates.add(cb.equal(root.<Boolean>get("enabled"), enabled));
				}

				if (updatedBy != null) {
					predicates.add(cb.equal(root.<String>get("updatedBy"), id));
				}

				if (lastUpdatedTime != null) {
					Date sd = lastUpdatedTime;
					Date ed = new Date();
					predicates.add(cb.and(cb.greaterThanOrEqualTo(root.<Date>get("lastUpdatedTime"), sd),
							cb.lessThan(root.<Date>get("lastUpdatedTime"), ed)));
				}

				return cb.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

}
