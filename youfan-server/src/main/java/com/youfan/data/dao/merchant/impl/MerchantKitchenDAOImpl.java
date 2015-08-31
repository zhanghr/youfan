package com.youfan.data.dao.merchant.impl;

import com.mongodb.DBCursor;
import com.youfan.commons.vo.MerchantKitchenInfoVO;
import com.youfan.data.dao.merchant.MerchantKitchenDAO;
import com.youfan.data.models.MerchantKitchenInfoEntity;
import com.youfan.exceptions.KitchenInfoException;
import com.youfan.utils.JSONUtils;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by perfection on 15-8-25.
 */
@Repository("merchantKitchenDAO")
public class MerchantKitchenDAOImpl implements MerchantKitchenDAO {

    @Override
    public MerchantKitchenInfoVO findOne(Long aLong) {
        return convertToVO(mongoTemplate.findOne(query(where("id").is(aLong)), getEntityClass()));
    }

    @Override
    public void insert(MerchantKitchenInfoVO merchantKitchenInfo) {
        mongoTemplate.insert(convertToEntity(merchantKitchenInfo));
    }

    @Override
    public void delete(Long aLong) {
        mongoTemplate.updateFirst(query(where("id").is(aLong)), Update.update("status", -1), getEntityClass());
    }

    @Override
    public void update(MerchantKitchenInfoVO merchantKitchenInfo) {
//        Update
//        mongoTemplate.findAndModify(query(where("id").is(merchantKitchenInfo.getId())),);
    }

    @Override
    public List<MerchantKitchenInfoVO> pageList(Integer page, Integer pageSize) throws KitchenInfoException {
        DBCursor limit = mongoTemplate.getCollection(COLLECTION_KITCHENINFO).find().skip((page - 1) * pageSize).limit(pageSize);
        MerchantKitchenInfoEntity merchantKitchenInfoEntity = null;
        List<MerchantKitchenInfoVO> list = new ArrayList<>();
        while (limit.hasNext()) {
            Map map = limit.next().toMap();
            String id = map.get("_id").toString();
            map.remove("_id");
            map.put("id", id);
            merchantKitchenInfoEntity = JSONUtils.map2pojo(map, getEntityClass());
            list.add(convertToVO(merchantKitchenInfoEntity));
        }
        return list;
    }

    @Override
    public MerchantKitchenInfoVO findById(String id) {
        Query q = new Query().addCriteria(Criteria.where("id").is(id));
        MerchantKitchenInfoEntity mre = mongoTemplate.findOne(q, getEntityClass());
        return convertToVO(mre);
    }

    @Override
    public MerchantKitchenInfoVO saveMerchantKitchenInfo(MerchantKitchenInfoVO merchantKitchenInfo) throws KitchenInfoException {
        //判断是否存在该表
        createCollection();

        Update update = new Update();
        String[] test = new String[2];
        test[0] = "川菜";
        test[1] = "鲁菜";

        update.set("cuisine", test);
//        update.set("desc", merchantKitchenInfo.getDesc());
        update.set("desc", "查你家的大水表");
//        update.set("disPrice", merchantKitchenInfo.getDisPrice());
        update.set("disPrice", 10);
//        update.set("disRange", merchantKitchenInfo.getDisRange());
        update.set("disRange", "2");
//        update.set("distribution", merchantKitchenInfo.getDistribution());
        update.set("distribution", "配送你一个大水表");
//        update.set("endTime", merchantKitchenInfo.getEndTime());
        update.set("endTime", "20:00");
//        update.set("kitchenAddress", merchantKitchenInfo.getKitchenAddress());
        update.set("kitchenAddress", "华阳金南园");
        update.set("kitchenName", "叶哥厨房");
        update.set("phoneNumber", "18328725827");
//        update.set("startTime", merchantKitchenInfo.getStartTime());
        update.set("startTime", "10:00");
//        update.set("galleryFul", merchantKitchenInfo.getGalleryFul());
//        update.set("isCanteen", merchantKitchenInfo.isCanteen());
        update.set("galleryFul", 10);
        update.set("isDistribution", merchantKitchenInfo.isDistribution());
        update.set("isTakeSelf", merchantKitchenInfo.isTakeSelf());
        update.set("lat", "30.507874");
        update.set("lng", "104.068527");
        merchantKitchenInfo.setId(new Long("55dad8b374d29345d56d8136"));
        return convertToVO(mongoTemplate.findAndModify(query(where("id").is(merchantKitchenInfo.getId())), update, getEntityClass()));
    }

    @Override
    public MerchantKitchenInfoVO saveMerchantKitchenPicInfo(MerchantKitchenInfoVO merchantKitchenInfo) throws KitchenInfoException {
        createCollection();

        Update update = new Update();

        update.set("kitchenPicUrl", merchantKitchenInfo.getKitchenPicUrl());

        return convertToVO(mongoTemplate.findAndModify(query(where("id").is(merchantKitchenInfo.getId())), update, getEntityClass()));
    }

    @Override
    public MerchantKitchenInfoVO saveMerchantKitchenStoryInfo(MerchantKitchenInfoVO merchantKitchenInfo) throws KitchenInfoException {
        createCollection();

        Update update = new Update();

        update.set("address", merchantKitchenInfo.getKitchenStoryName());
        update.set("ageRange", merchantKitchenInfo.getKitchenStoryContent());

        return convertToVO(mongoTemplate.findAndModify(query(where("id").is(merchantKitchenInfo.getId())), update, getEntityClass()));
    }

    /**
     * // TODO MongoDB 不需要预先创建collection, collection会在第一次插入文档的时候自动创建.
     */
    private void createCollection() {
        if (!mongoTemplate.collectionExists(getEntityClass())) {
            mongoTemplate.createCollection(getEntityClass());
        }
    }

    @Override
    public long count(Query query) {
        return mongoTemplate.count(query, getEntityClass());
    }

    @Override
    public List<MerchantKitchenInfoEntity> find(Query query) {
        return mongoTemplate.find(query, getEntityClass());
    }
}
