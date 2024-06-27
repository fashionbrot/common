package com.github.fashionbrot.common.TLVBuffer.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> implements Serializable {

    private static final long serialVersionUID = -1314205697700918198L;

    private List<T> rows;

    private long total;

}
