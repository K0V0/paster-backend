package com.kovospace.paster.base.websockets.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WsAutosyncReplyDTO extends WsReplyDTO {

  private boolean autosync;

}
