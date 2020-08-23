package com.test.httpforward.fwdserver.urlReg.controller;

import com.test.fwdcommon.entity.ResultModel;
import com.test.fwdcommon.entity.enums.PubResultEnum;
import com.test.httpforward.fwdserver.urlReg.bean.UrlRegDto;
import com.test.httpforward.fwdserver.urlReg.bean.UrlRegVo;
import com.test.httpforward.fwdserver.urlReg.service.IUrlRegService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 楚休红
 * @since 2020-04-20
 */
@RestController
@RequestMapping
@Slf4j
public class UserController {
    @Autowired
    IUrlRegService urlRegService;

    /**
     * 注册代理信息
     * @param dto
     * @return
     */
    @PostMapping("reg")
    public ResultModel<UrlRegVo> reg(@RequestBody UrlRegDto dto) {
        return urlRegService.regist(dto);
    }

    @GetMapping("getAll")
    public ResultModel<Collection<UrlRegVo>> getAll() {
        return PubResultEnum.SUCC.data(urlRegService.getAll());
    }

    @GetMapping("cancel")
    public ResultModel<Void> cancel(String certificates) {
        urlRegService.cancel(certificates);
        return PubResultEnum.SUCC.msg();
    }
}

