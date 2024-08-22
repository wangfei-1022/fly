package com.wf.imaotai.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.wf.imaotai.entity.Item;
import com.wf.imaotai.mapper.ItemMapper;
import com.wf.imaotai.model.dto.SelectionDTO;
import com.wf.imaotai.model.dto.SelectionI;
import com.wf.imaotai.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    public ItemMapper itemMapper;

    @Autowired
    public RestTemplate restTemplate;

    @Override
    public List<Item> list(Item item) {
        PageHelper.startPage(1, 10);
        List<Item> xx1 = itemMapper.getList();
        return xx1;
    }


    /*
    * https://static.moutai519.com.cn/mt-backend/xhr/front/mall/index/session/get/1724256000000
    * {"indexShowInfo":{"backgroundPicList":["https://resource.moutai519.com.cn/mt-resource/static-union/164016569047550f.png"],"blockchainPolicy":{"logo":"https://resource.moutai519.com.cn/mt-resource/static-union/1650339161fbd333.png","title":"区块链端摇号规则","content":[{"subtitle":"","desc":["1.系统接收预约申购用户数据，为用户分配区块链上的唯一专用申购码。","2.申购码经区块链专业哈希算法处理后链上留存备用。","3.每天预约结束后，摇号程序自动开始，区块链上的申购智能合约被触发，系统自动生成本场次申购成功因子。","4.区块链上的申购匹配算法被触发，根据本场次申购成功因子选出申购成功的申购码。","5.系统自动调用公证服务（杭州互联网公证处）自动生成本场次参加申购的每一家门店数据公证文件。","6.每日 18:00 公布申购结果，同期预约登记成功的用户可在终端查询公证结果。"]}]},"backgroundLargerPicList":["https://resource.moutai519.com.cn/mt-resource/static-union/165223442501a225.png"],"banner":[{"pictureUrlV2":"https://resource.moutai519.com.cn/mt-resource/static-union/1724027277080a63.png","reservePictureUrl":"https://resource.moutai519.com.cn/mt-resource/static-union/172402727718b734.png","jumpUrl":"moutaiapp://webview?url=https%3A%2F%2Fh5.moutai519.com.cn%2Fact%2Fi%2Fxfjd%2Findex%3FappConfig%3D2_1_2%26_from%3Dxunfengeservationentrancebanner"}],"title":"享约·茅台","itemBackgroundPic":"https://resource.moutai519.com.cn/mt-resource/static-union/165154694880f0a6.png","waitReservePicUrl":"https://resource.moutai519.com.cn/mt-resource/static-union/1656490066006f33.png","blockchainPolicySpecial":{"logo":"https://resource.moutai519.com.cn/mt-resource/static-union/1650339161fbd333.png","title":"区块链端摇号规则","content":[{"subtitle":"","desc":["1.系统接收预约申购用户数据，为用户分配区块链上的唯一专用申购码。","2.申购码经区块链专业哈希算法处理后链上留存备用。","3.预约结束后，摇号程序自动开始，区块链上的申购智能合约被触发，系统自动生成本场次申购成功因子。","4.区块链上的申购匹配算法被触发，根据本场次申购成功因子选出申购成功的申购码。","5.系统自动调用公证服务（杭州互联网公证处）自动生成本场次参加申购的每一家门店数据公证文件。","6.公布申购结果，同期预约登记成功的用户可在终端查询公证结果。"]}]},"waitReserveDesc":"距离下一场申购开启还有","reservationPolicy":{"logo":"https://resource.moutai519.com.cn/mt-resource/static-union/1650339161fbd333.png","title":"申购规则","content":[{"subtitle":"一、参与形式","desc":["为尽可能满足广大消费者的购酒需求，i茅台平台【享约·申购】页面商品均采用申购制，凡符合申购条件的用户均可进行申购。"]},{"subtitle":"二、预约申购规则","desc":["1.预约申购条件:年满18周岁以上且具备完全民事行为能力的人可参与预约登记。预约登记采用身份信息实名认证，每一身份证号码绑定一个专属账号。登记成功后即可使用该专属账号进行申购。","2.预约申购的范围:【享约·申购】页面显示的所有商品。","3.预约申购时间:每天上午9:00-10:00，以i茅台平台首页显示时间为准。预约申购登记先后顺序与申购成功率不关联。","4.申购登记:进入申购页面，根据您的需求选择门店点击申购商品和数量，即可提交登记。每一实名认证的账号，每场次每品种只能申购1次。","5.申购状态说明:根据申购进程，将呈现静候申购结果;申购成功;申购失败;申购失效等4种申购状态。","6.当您申购成功后，应在申购成功次日18时之前选择付款方式，如您选择线上付款，应在选定付款方式提示的付款期限内完成付款。","7.支付成功后，提货码会在14天内生成，当您收到提货码后，请您携带本人身份证在7天以内到预约门店提货，如您因不可抗力原因(即不能预见、不能避免并不能克服的客观情况)无法在截止时间前到预约门店提货，请您与提货门店另行友好协商提货时间。申购成功的用户需本人前往门店提货，提货时需出示身份证完成实人认证方可提货，不允许他人代提。"]}]}},"sessionStatus":[{"buttonText":"09:00开始申购","timeDescPicUrl":"https://resource.moutai519.com.cn/mt-resource/static-union/16390332870a159f.png","startTime":0,"endTime":1724288400000,"tips":"8月22日09:00~10:00开放预约","status":2},{"changeText":"查看申购详情","buttonText":"预约申购","timeDescPicUrl":"https://resource.moutai519.com.cn/mt-resource/static-union/16390332936e6ab3.png","startTime":1724288400000,"endTime":1724292000000,"tips":"09:00~10:00预约申购进行中","status":1},{"buttonText":"本场申购已结束","timeDescPicUrl":"https://resource.moutai519.com.cn/mt-resource/static-union/1639033297862f0e.png","startTime":1724292000000,"endTime":1724320800000,"tips":"本场申购已结束，8月22日18:00公示结果","status":3},{"buttonText":"申购结果公示","timeDescPicUrl":"https://resource.moutai519.com.cn/mt-resource/static-union/16390333003c7d90.png","startTime":1724320800000,"endTime":1724407200000,"tips":"本场申购已结束，8月22日18:00公示结果","status":4},{"buttonText":"09:00开始申购","timeDescPicUrl":"https://resource.moutai519.com.cn/mt-resource/static-union/16390332870a159f.png","startTime":1724407200000,"endTime":4611686018427387903,"tips":"暂无场次","status":2}],"itemList":[{"itemCode":"10941","pictureV2":"https://resource.moutai519.com.cn/mt-resource/static-union/17044457510f985f.png","checkTag":0,"count":0,"title":"贵州茅台酒 (甲辰龙年)","picture":"https://resource.moutai519.com.cn/mt-resource/static-union/164743876053f93c.png","content":"龍腾九霄，四海安澜。2024年甲辰至，贵州茅台推出甲辰龙年贵州茅台酒，集茅台文化，五行文化，生肖文化，书画艺术之大成、鉴藏皆宜。","jumpUrl":"moutaiapp://webview?url=https%3A%2F%2Fh5.moutai519.com.cn%2Fact%2Fi%2FactPage%3Fqurl%3D1704445097de65e5.json%26appConfig%3D2_1_2"},{"itemCode":"10923","pictureV2":"https://resource.moutai519.com.cn/mt-resource/static-union/17159329443a69fd.jpg","checkTag":0,"count":0,"title":"53%vol 500ml茅台1935·中国国家地理文创酒","picture":"https://resource.moutai519.com.cn/mt-resource/static-union/164743876053f93c.png","content":"","jumpUrl":"moutaiapp://webview?url=https%3A%2F%2Fh5.moutai519.com.cn%2Fact%2Fi%2FactPage%3Fqurl%3D17168850009e4455.json%26appConfig%3D2_1_2"},{"itemCode":"2478","pictureV2":"https://resource.moutai519.com.cn/mt-resource/static-union/16844021797c9cb2.png","checkTag":0,"count":0,"title":"贵州茅台酒（珍品）","picture":"https://resource.moutai519.com.cn/mt-resource/static-union/16480897792e8039.png","content":"珍酿之品，百福具臻。沿袭初心至真、酒品至珍、风格至臻的理念，汇聚大师匠心，香气完美融合，酒体平衡协调。","jumpUrl":"moutaiapp://webview?url=https%3A%2F%2Fh5.moutai519.com.cn%2Fmt%2Ffind%3Fid%3D45%26appConfig%3D1_1_2%26articleId%3D1101"},{"itemCode":"10942","pictureV2":"https://resource.moutai519.com.cn/mt-resource/static-union/170444575066de2c.png","checkTag":0,"count":0,"title":"贵州茅台酒 (甲辰龙年)","picture":"https://resource.moutai519.com.cn/mt-resource/static-union/164743876053f93c.png","content":"龍腾九霄，四海安澜。2024年甲辰至，贵州茅台推出甲辰龙年贵州茅台酒，集茅台文化，五行文化，生肖文化，书画艺术之大成、鉴藏皆宜。","jumpUrl":"moutaiapp://webview?url=https%3A%2F%2Fh5.moutai519.com.cn%2Fact%2Fi%2FactPage%3Fqurl%3D1704446791083570.json%26appConfig%3D2_1_2"}],"sessionId":1163}
     * */
    @Override
    public String getCurrentSessionId() {
        String mtSessionId = "";
        long dayTime = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String res = restTemplate.getForObject("https://static.moutai519.com.cn/mt-backend/xhr/front/mall/index/session/get/" + dayTime, String.class);
        //替换 current_session_id 673 ['data']['sessionId']
        JSONObject jsonObject = JSONObject.parseObject(res);

        if (jsonObject.getString("code").equals("2000")) {
            JSONObject data = jsonObject.getJSONObject("data");
            System.out.println(data);
            System.out.println("https://static.moutai519.com.cn/mt-backend/xhr/front/mall/index/session/get/" + dayTime);
            mtSessionId = data.getString("sessionId");
//            itemMapper.truncateItem();
            //item插入数据库
//            JSONArray itemList = data.getJSONArray("itemList");
//            for (Object obj : itemList) {
//                JSONObject item = (JSONObject) obj;
//                Item shopItem = new Item("", item);
//                itemMapper.addItem(shopItem);
//            }
        }
        return mtSessionId;
    }

    @Override
    public List<SelectionDTO> convertSelection(SelectionI[] enums) {
        return Arrays.stream(enums).map(enumValue -> new SelectionDTO(enumValue.getCode(), enumValue.getName()))
                .collect(Collectors.toList());
    }
}
