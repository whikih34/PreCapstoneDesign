package com.springboot.apiserver.ppurio.ppuriodto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PpurioFileDto {
    private String name;
    private int size;
    private String data;
    @Override
    public String toString() {
        // Base64 데이터는 길이를 제한해서 출력
        String trimmedData = data != null ? data.substring(0, Math.min(data.length(), 100)) + "..." : "null";
        return String.format("PpurioFileDto{name='%s', size=%d, data='%s'}", name, size, trimmedData);
    }

}
