/*******************************************************************************
 * Copyright 2017 General Electric Company
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package com.ge.predix.acs.privilege.management.dao;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.ge.predix.acs.model.Attribute;
import com.ge.predix.acs.rest.Parent;
import com.ge.predix.acs.zone.management.dao.ZoneEntity;

@Entity
@Table(
        name = "subject",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "authorization_zone_id", "subject_identifier" }) })
public class SubjectEntity implements ZonableEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "authorization_zone_id", referencedColumnName = "id", nullable = false, updatable = false)
    private ZoneEntity zone;

    @Column(name = "subject_identifier", nullable = false)
    private String subjectIdentifier;

    /**
     * Clob representing set of attributes as a JSON body.
     */
    @Column(name = "attributes", columnDefinition = "CLOB NOT NULL")
    private String attributesAsJson = "{}";

    @Transient
    private Set<Attribute> attributes = Collections.emptySet();

    @Transient
    private Set<Parent> parents = Collections.emptySet();

    /**
     * Note about all these Id's and identifiers:
     *
     * id: surrogate id generated by the rbdms, intended to FK references, etc. subjectIdentifier: The actual subject
     * identifier being protected, Ex: dave@ge.com
     */

    public SubjectEntity() {
    }

    public SubjectEntity(final ZoneEntity zone, final String subjectIdentifier) {
        this.zone = zone;
        this.subjectIdentifier = subjectIdentifier;
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public ZoneEntity getZone() {
        return this.zone;
    }

    @Override
    public void setZone(final ZoneEntity zone) {
        this.zone = zone;
    }

    public String getSubjectIdentifier() {
        return this.subjectIdentifier;
    }

    public void setSubjectIdentifier(final String subjectIdentifier) {
        this.subjectIdentifier = subjectIdentifier;
    }

    @Override
    public String getAttributesAsJson() {
        return this.attributesAsJson;
    }

    @Override
    public void setAttributesAsJson(final String attributesAsJson) {
        this.attributesAsJson = attributesAsJson;
    }

    @Override
    public Set<Attribute> getAttributes() {
        return this.attributes;
    }

    @Override
    public void setAttributes(final Set<Attribute> attributes) {
        if (attributes == null) {
            this.attributes = Collections.emptySet();
        } else {
            this.attributes = attributes;
        }
    }

    @Override
    public Set<Parent> getParents() {
        return this.parents;
    }

    @Override
    public void setParents(final Set<Parent> parents) {
        this.parents = parents;
    }

    public void setId(final long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SubjectEntity [id=" + this.id + ", zone=" + this.zone + ", subjectIdentifier=" + this.subjectIdentifier
                + ", attributesAsJson=" + this.attributesAsJson + ", parents=" + this.parents + "]";
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.subjectIdentifier).append(this.zone).append(this.attributesAsJson)
                .append(this.parents).toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SubjectEntity) {
            SubjectEntity other = (SubjectEntity) obj;
            return new EqualsBuilder().append(this.subjectIdentifier, other.subjectIdentifier)
                    .append(this.zone, other.zone).append(this.attributesAsJson, other.attributesAsJson)
                    .append(this.parents, other.parents).isEquals();
        }
        return false;
    }

    @Override
    public String getEntityId() {
        return this.getSubjectIdentifier();
    }

    @Override
    public String getEntityType() {
        return "subject";
    }
}
