package com.steve.erputill.file;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FileRepository {

    private final EntityManager em;


    public void save(FileInfoEntity file) {
        em.persist(file);
    }

    public FileInfoEntity findOne(String name) {
        return em.find(FileInfoEntity.class, name);
    }

    public List<FileInfoEntity> findByUsage(FileUsage usage){
        return em.createQuery("select f from FileInfoEntity f where f.usage = :usage", FileInfoEntity.class)
                .setParameter("usage", usage)
                .getResultList();
    }

    public void delete(FileInfoEntity fileInfo){
        em.remove(fileInfo);
    }





}
