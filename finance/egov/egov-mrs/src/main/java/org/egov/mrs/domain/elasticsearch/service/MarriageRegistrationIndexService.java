/**
 * eGov suite of products aim to improve the internal efficiency,transparency,
   accountability and the service delivery of the government  organizations.

    Copyright (C) <2016>  eGovernments Foundation

    The updated version of eGov suite of products as by eGovernments Foundation
    is available at http://www.egovernments.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see http://www.gnu.org/licenses/ or
    http://www.gnu.org/licenses/gpl.html .

    In addition to the terms of the GPL license to be adhered to in using this
    program, the following additional terms are to be complied with:

        1) All versions of this program, verbatim or modified must carry this
           Legal Notice.

        2) Any misrepresentation of the origin of the material is prohibited. It
           is required that all modified versions of this material be marked in
           reasonable ways as different from the original version.

        3) This license does not grant any rights to any user of the program
           with regards to rights under trademark law for use of the trade names
           or trademarks of eGovernments Foundation.

  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */
package org.egov.mrs.domain.elasticsearch.service;

import java.math.BigDecimal;
import java.util.Date;

import org.egov.config.search.Index;
import org.egov.config.search.IndexType;
import org.egov.infra.admin.master.entity.City;
import org.egov.infra.admin.master.service.CityService;
import org.egov.infra.config.core.ApplicationThreadLocals;
import org.egov.infra.search.elastic.annotation.Indexing;
import org.egov.mrs.domain.elasticsearch.entity.MarriageRegistrationSearch;
import org.egov.mrs.domain.entity.MarriageCertificate;
import org.egov.mrs.domain.entity.MarriageRegistration;
import org.egov.mrs.domain.entity.MarriageWitness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MarriageRegistrationIndexService {

	@Autowired
	private CityService cityService;

	@Indexing(name = Index.MARRIAGEREGISTRATION, type = IndexType.MARRIAGEREGISTRATIONSEARCH)
	public MarriageRegistrationSearch createMarriageIndex(final MarriageRegistration registration,
			final String applicationType) {

		final City cityWebsite = cityService.getCityByURL(ApplicationThreadLocals.getDomainName());

		MarriageRegistrationSearch registrationSearch = new MarriageRegistrationSearch(registration.getApplicationNo(),
				cityWebsite.getName(), cityWebsite.getCode(), registration.getCreatedDate(),
				cityWebsite.getDistrictName(), cityWebsite.getRegionName(), cityWebsite.getGrade());
		if (registration != null) {
			registrationSearch
					.setApplicationNo(registration.getApplicationNo() != null ? registration.getApplicationNo() : "");
			registrationSearch.setApplicationType(applicationType);
			registrationSearch.setDateOfMarriage(
					registration.getDateOfMarriage() != null ? registration.getDateOfMarriage() : new Date());
			registrationSearch.setApplicationCreatedBy(
					registration.getCreatedBy().getName() != null ? registration.getCreatedBy().getName() : "");
			registrationSearch
					.setZone(registration.getZone().getName() != null ? registration.getZone().getName() : "");
			registrationSearch.setMarriageAct(registration.getMarriageAct().getDescription() != null
					? registration.getMarriageAct().getDescription() : "");
			registrationSearch.setPlaceOfMarriage(
					registration.getPlaceOfMarriage() != null ? registration.getPlaceOfMarriage() : "");
			registrationSearch.setMarriageFeeCriteria(registration.getFeeCriteria().getCriteria() != null
					? registration.getFeeCriteria().getCriteria() : "");
			registrationSearch.setMarriageFeeAmount(registration.getFeeCriteria().getFees() != null
					? BigDecimal.valueOf(registration.getFeeCriteria().getFees()) : BigDecimal.ZERO);

			if (registration.getHusband() != null) {
				registrationSearch.setHusbandName(registration.getHusband().getFullName());
				registrationSearch.setHusbandReligion(registration.getHusband().getReligion() != null
						? registration.getHusband().getReligion().getDescription() : "");
				registrationSearch
						.setHusbandAgeInYearsAsOnMarriage(registration.getHusband().getAgeInYearsAsOnMarriage() != null
								? Double.valueOf(registration.getHusband().getAgeInYearsAsOnMarriage()) : 0.0d);
				registrationSearch.setHusbandAgeInMonthsAsOnMarriage(
						registration.getHusband().getAgeInMonthsAsOnMarriage() != null
								? Double.valueOf(registration.getHusband().getAgeInMonthsAsOnMarriage()) : 0.0d);
				registrationSearch.setHusbandMaritalStatus(registration.getHusband().getMaritalStatus().name() != null
						? registration.getHusband().getMaritalStatus().name() : "");
				registrationSearch
						.setHusbandReligionPractice(registration.getHusband().getReligionPractice().name() != null
								? registration.getHusband().getReligionPractice().name() : "");
				registrationSearch.setHusbandOccupation(registration.getHusband().getOccupation() != null
						? registration.getHusband().getOccupation() : "");
				registrationSearch.setHusbandPhoneNo(registration.getHusband().getContactInfo() != null
						? registration.getHusband().getContactInfo().getMobileNo() : "");
				registrationSearch.setHusbandAadhaarNo(registration.getHusband().getAadhaarNo() != null
						? registration.getHusband().getAadhaarNo() : "");
				registrationSearch.setHusbandResidencyAddress(
						registration.getHusband().getContactInfo().getResidenceAddress() != null
								? registration.getHusband().getContactInfo().getResidenceAddress() : "");
				registrationSearch
						.setHusbandOfficeAddress(registration.getHusband().getContactInfo().getOfficeAddress() != null
								? registration.getHusband().getContactInfo().getOfficeAddress() : "");
				registrationSearch.setHusbandEmail(registration.getHusband().getContactInfo().getEmail() != null
						? registration.getHusband().getContactInfo().getEmail() : "");
			}

			if (registration.getWife() != null) {
				registrationSearch.setWifeName(registration.getWife().getFullName());
				registrationSearch.setWifeReligion(registration.getWife().getReligion() != null
						? registration.getWife().getReligion().getDescription() : "");
				registrationSearch
						.setWifeAgeInYearsAsOnMarriage(registration.getWife().getAgeInYearsAsOnMarriage() != null
								? Double.valueOf(registration.getWife().getAgeInYearsAsOnMarriage()) : 0.0d);
				registrationSearch
						.setWifeAgeInMonthsAsOnMarriage(registration.getWife().getAgeInMonthsAsOnMarriage() != null
								? Double.valueOf(registration.getWife().getAgeInMonthsAsOnMarriage()) : 0.0d);

				registrationSearch.setWifeMaritalStatus(registration.getWife().getMaritalStatus() != null
						? registration.getWife().getMaritalStatus().name() : "");
				registrationSearch.setWifeReligionPractice(registration.getWife().getReligionPractice().name() != null
						? registration.getWife().getReligionPractice().name() : "");
				registrationSearch.setWifeOccupation(
						registration.getWife().getOccupation() != null ? registration.getWife().getOccupation() : "");
				registrationSearch.setWifePhoneNo(registration.getWife().getContactInfo().getMobileNo() != null
						? registration.getWife().getContactInfo().getMobileNo() : "");
				registrationSearch.setWifeAadhaarNo(
						registration.getWife().getAadhaarNo() != null ? registration.getWife().getAadhaarNo() : "");
				registrationSearch
						.setWifeResidencyAddress(registration.getWife().getContactInfo().getResidenceAddress() != null
								? registration.getWife().getContactInfo().getResidenceAddress() : "");
				registrationSearch
						.setWifeOfficeAddress(registration.getWife().getContactInfo().getOfficeAddress() != null
								? registration.getWife().getContactInfo().getOfficeAddress() : "");
				registrationSearch.setWifeEmail(registration.getWife().getContactInfo().getEmail() != null
						? registration.getWife().getContactInfo().getEmail() : "");
			}

			if (!registration.getWitnesses().isEmpty()) {
				MarriageWitness witness1 = registration.getWitnesses().get(0);
				if (witness1 != null) {
					registrationSearch.setWitness1Name(witness1.getFullName());
					registrationSearch
							.setWitness1AadhaarNo(witness1.getAadhaarNo() != null ? witness1.getAadhaarNo() : "");
					registrationSearch
							.setWitness1Occupation(witness1.getOccupation() != null ? witness1.getOccupation() : "");
					registrationSearch.setWitness1Address(witness1.getContactInfo().getResidenceAddress() != null
							? witness1.getContactInfo().getResidenceAddress() : "");
					registrationSearch
							.setWitness1RelationshipWithApplicant(witness1.getRelationshipWithApplicant() != null
									? witness1.getRelationshipWithApplicant() : "");
				}

				MarriageWitness witness2 = registration.getWitnesses().get(1);
				if (witness2 != null) {
					registrationSearch.setWitness2Name(witness2.getFullName());
					registrationSearch
							.setWitness2AadhaarNo(witness2.getAadhaarNo() != null ? witness2.getAadhaarNo() : "");
					registrationSearch
							.setWitness2Occupation(witness2.getOccupation() != null ? witness2.getOccupation() : "");
					registrationSearch.setWitness2Address(witness2.getContactInfo().getResidenceAddress() != null
							? witness2.getContactInfo().getResidenceAddress() : "");
					registrationSearch
							.setWitness2RelationshipWithApplicant(witness2.getRelationshipWithApplicant() != null
									? witness2.getRelationshipWithApplicant() : "");
				}

				MarriageWitness witness3 = registration.getWitnesses().get(2);
				if (witness3 != null) {
					registrationSearch.setWitness3Name(witness3.getFullName());
					registrationSearch
							.setWitness3AadhaarNo(witness3.getAadhaarNo() != null ? witness3.getAadhaarNo() : "");
					registrationSearch
							.setWitness3Occupation(witness3.getOccupation() != null ? witness3.getOccupation() : "");
					registrationSearch.setWitness3Address(witness3.getContactInfo().getResidenceAddress() != null
							? witness3.getContactInfo().getResidenceAddress() : "");
					registrationSearch
							.setWitness3RelationshipWithApplicant(witness3.getRelationshipWithApplicant() != null
									? witness3.getRelationshipWithApplicant() : "");
				}
			}

			if (registration.getPriest() != null && registration.getPriest().getName() != null) {
				registrationSearch.setPriestName(registration.getPriest().getName().getFirstName() != null
						? registration.getPriest().getName().getFirstName() : "");
				// registrationSearch.setPriestAge(registration.getPriest().getAge().toString()!=
				// null ?registration.getPriest().getAge().toString():"");
				registrationSearch
						.setPriestAddress(registration.getPriest().getContactInfo().getResidenceAddress() != null
								? registration.getPriest().getContactInfo().getResidenceAddress() : "");
				registrationSearch.setPriestReligion(registration.getPriest().getReligion().getDescription() != null
						? registration.getPriest().getReligion().getDescription() : "");
			}
			for (MarriageCertificate certificate : registration.getMarriageCertificate()) {
				if (certificate != null) {
					registrationSearch.setCertificateNo(
							certificate.getCertificateNo() != null ? certificate.getCertificateNo() : "");
					registrationSearch.setCertificateType(
							certificate.getCertificateType() != null ? certificate.getCertificateType() : "");
					registrationSearch.setCertificateDate(certificate.getCertificateDate());
					registrationSearch.setCertificateIssued(certificate.isCertificateIssued());
				}
			}
			registrationSearch.setActive(registration.isActive());
			registrationSearch.setApplicationStatus(
					registration.getStatus().getDescription() != null ? registration.getStatus().getDescription() : "");
			registrationSearch.setRejectionReason(
					registration.getRejectionReason() != null ? registration.getRejectionReason() : "");
			registrationSearch.setRemarks(registration.getRemarks() != null ? registration.getRemarks() : "");
		}
		return registrationSearch;
	}

}