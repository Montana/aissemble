package com.boozallen.aiops.mda.metamodel.element.util;

/*-
 * #%L
 * aiSSEMBLE::Foundation::MDA
 * %%
 * Copyright (C) 2021 Booz Allen
 * %%
 * This software package is licensed under the Booz Allen Public License. All Rights Reserved.
 * #L%
 */

//import com.boozallen.aissemble.data.encryption.policy.json.EncryptionPolicyInput;
import com.boozallen.aissemble.data.encryption.policy.json.EncryptionPolicyInput;

import java.util.Collections;
import java.util.List;

/**
 * Helps with encryption of nested fields
 */
public class RecordFieldEncryptionUtil {

    public RecordFieldEncryptionUtil() {
    }

    /***
     * This method constructs a full Spark transform of the given fields and ensures
     * fields are wrapped in an encryption UDF wrapper when a field requires encryption
     * @param encryptionPolicyInput A list of record fields
     * @return the full transform statement
     */
    public String generateFieldEncryptionTransform(List<String> fieldIds, EncryptionPolicyInput encryptionPolicyInput) {
        Collections.sort(fieldIds);
        StringBuilder sb = new StringBuilder();
        sb.append("transform(data, x -> struct(");
        Boolean nested = false;

        for(String aFieldId: fieldIds) {
            if(aFieldId.contains(".")){
                if(!nested) {
                    nested = true;
                    sb.append("struct(");
                }
                expandFieldNotation(aFieldId, encryptionPolicyInput, sb);

            } else {
                if(nested) {
                    nested = false;
                    sb.append(")");
                }
                expandFieldNotation(aFieldId, encryptionPolicyInput, sb);
            }
        }
        if(nested) {
            sb.append(")");
        }

        sb.append(" as data))");

        String transformStatement = sb.toString();
        transformStatement = transformStatement.replaceAll(", \\)", ")");

        return transformStatement;
    }

    /***
     * This method expands a field into a Spark transform notation
     * for example: the field monotonic_time becomes x.monotonic_time as monotonic_time
     * or in the case where the field needs to be encrypted
     * encryptUDF(x.monotonic_time, "AES_ENCRYPT") as monotonic_time
     * @param aFieldId
     * @param encryptionPolicyInput contains the encrypt fields and encrypt algorithm
     * @param sb
     */
    private void expandFieldNotation(String aFieldId, EncryptionPolicyInput encryptionPolicyInput, StringBuilder sb) {
        sb.append(checkFieldForEncryption(aFieldId, encryptionPolicyInput));
        sb.append(" as ");
        if(aFieldId.contains(".")) {
            String fieldName = getFieldFromFullyQualifiedPath(aFieldId);
            sb.append(fieldName);
        } else {
            sb.append(aFieldId);
        }
        sb.append(", ");
    }

    /***
     * This method checks if a field requires encryption.  If it does need encryption it will be
     * wrapped in a UDF syntax. for example: encryptUDF(x.data.ssn, "AES_ENCRYPT")
     * @param aFieldId the field name (including the dot path if it is a nested field)
     * @param encryptionPolicyInput contains the encrypt fields and encrypt algorithm
     * @return
     */
    private String checkFieldForEncryption(String aFieldId, EncryptionPolicyInput encryptionPolicyInput) {
        assert encryptionPolicyInput.getEncryptAlgorithm() != null;
        StringBuilder sb = new StringBuilder();

        if(encryptionPolicyInput.getEncryptFields() != null && encryptionPolicyInput.getEncryptFields().contains(aFieldId)) {
            sb.append("encryptUDF(x.");
            sb.append(aFieldId);
            sb.append(",");
            sb.append("\"" + encryptionPolicyInput.getEncryptAlgorithm().toString() + "\")");
        } else {
            sb.append("x.");
            sb.append(aFieldId);
        }

        return sb.toString();
    }

    /***
     * Gets the field name from a dot path field representation
     * for example data.ssn --> ssn
     * @param fullyQualifiedPath full path to the field using dot notation where the field is nested
     * @return the field name
     */
    private String getFieldFromFullyQualifiedPath(String fullyQualifiedPath) {
        int lastDotIndex = fullyQualifiedPath.lastIndexOf(".");

        return fullyQualifiedPath.substring(lastDotIndex + 1);
    }

}
