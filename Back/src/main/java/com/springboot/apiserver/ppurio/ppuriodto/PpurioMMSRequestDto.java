package com.springboot.apiserver.ppurio.ppuriodto;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PpurioMMSRequestDto {
    private String account; //service set
    private String messageType; //service set
    private String content;
    private String from; //service set
    private String duplicateFlag;
    private String imgUrl;
    private String subject; //service set -- mms에만 있음
    private int targetCount;
    private List<PpurioTargetDto> targets;
    private String refKey = "MMS_REF_KEY";
    private List<PpurioFileDto> files;

    public Map<String, Object> toMap() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("account",account);
        params.put("from",from);
        params.put("messageType", messageType);
        params.put("content", content);
        params.put("duplicateFlag", duplicateFlag);
        params.put("targetCount", targetCount);
        params.put("targets", targets);
        params.put("refKey", refKey);
        params.put("files",files);
        return params;
    }
    @Override
    public String toString() {
        return String.format(
                "PpurioMMSRequestDto{account='%s', messageType='%s', content='%s', from='%s', duplicateFlag='%s', imgUrl='%s', targetCount=%d, targets=%s, refKey='%s', files=%s}",
                account, messageType, content, from, duplicateFlag, imgUrl, targetCount, targets, refKey, files
        );
    }


}
