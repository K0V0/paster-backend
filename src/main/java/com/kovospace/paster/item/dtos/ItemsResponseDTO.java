package com.kovospace.paster.item.dtos;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemsResponseDTO<T> extends OkResponseDTO {
  private List<T> items;
}
