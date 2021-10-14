package com.kovospace.paster.item.dtos;

import com.kovospace.paster.base.dtos.OkResponseDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemsResponseDTO extends OkResponseDTO {
  private List<ItemResponseDTO> items;
}
