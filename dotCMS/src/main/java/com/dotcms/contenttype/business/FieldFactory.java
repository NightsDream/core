package com.dotcms.contenttype.business;

import java.util.List;

import com.dotcms.contenttype.model.field.Field;
import com.dotcms.contenttype.model.field.FieldVariable;
import com.dotcms.contenttype.model.type.ContentType;
import com.dotmarketing.exception.DotDataException;
import java.util.Optional;

public interface FieldFactory {
    default FieldFactory instance() {
        return new FieldFactoryImpl();
    }

    public final static String GENERIC_FIELD_VAR="field";
    Field byId(String id) throws DotDataException;

    Optional<Field> byContentTypeIdFieldRelationTypeInDb(String id, String var) throws DotDataException;

    List<Field> byContentType(ContentType type) throws DotDataException;

    List<Field> byContentTypeVar(String var) throws DotDataException;


    Field save(Field field) throws DotDataException;


    /**
     * Searches for all field variables whose key match the parameter
     * @param key
     * @return
     * @throws DotDataException
     */
    List<FieldVariable> byFieldVariableKey(String key) throws DotDataException;

    void delete(Field field) throws DotDataException;

    List<Field> byContentTypeId(String id) throws DotDataException;


    void deleteByContentType(ContentType type) throws DotDataException;


    Field byContentTypeFieldVar(ContentType type, String var) throws DotDataException;


    Field byContentTypeIdFieldVar(String id, String var) throws DotDataException;


    List<Field> selectByContentTypeInDb(String id) throws DotDataException;


    String suggestVelocityVar(String tryVar, List<Field> takenFields) throws DotDataException;


    FieldVariable save(FieldVariable fieldVar) throws DotDataException;

    void delete(FieldVariable fieldVar) throws DotDataException;

    List<FieldVariable> loadVariables(Field field) throws DotDataException;
    
    FieldVariable loadVariable(String id) throws DotDataException;


    String nextAvailableColumn(Field field) throws DotDataException;


    public void moveSortOrderForward(String contentTypeId, int from, int to) throws DotDataException;

    public void moveSortOrderBackward(String contentTypeId, int from, int to) throws DotDataException;

    public void moveSortOrderForward(String contentTypeId, int from) throws DotDataException;

    public void moveSortOrderBackward(String contentTypeId, int to) throws DotDataException;

}
