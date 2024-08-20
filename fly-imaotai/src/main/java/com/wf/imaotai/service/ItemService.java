package com.wf.imaotai.service;


import com.wf.imaotai.entity.Item;
import com.wf.imaotai.model.dto.SelectionDTO;
import com.wf.imaotai.model.dto.SelectionI;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    List<Item> list(Item item);

    String getCurrentSessionId();


    List<SelectionDTO> convertSelection(SelectionI[] enums);

}
