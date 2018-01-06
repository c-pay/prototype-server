package com.prototype.server.prototypeserver.repository;

import com.prototype.server.prototypeserver.entity.Section;
import com.prototype.server.prototypeserver.entity.TypeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("sectionRepository")
public interface SectionRepository extends JpaRepository<Section, Integer> {

}
