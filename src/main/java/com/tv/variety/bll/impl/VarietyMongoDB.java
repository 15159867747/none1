package com.tv.variety.bll.impl;


import com.tv.variety.bll.IVarietyMongoDB;
import com.tv.variety.dto.SearchVarietyparams;
import com.tv.variety.mongodb.POJO.Variety;
import com.tv.variety.param.VarietyParams;
import com.tv.variety.util.mongodb.MongoPageHelper;
import com.tv.variety.util.mongodb.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @author yrongqin@linwell.com
 * @createtime ${date}${time}
 */
@Component
//@Service
public class VarietyMongoDB implements IVarietyMongoDB {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private MongoPageHelper mongoPageHelper;




    @Override
    public Variety findVarietyByName(String name) {
        Query query=new Query(Criteria.where("name").is(name));
        Variety vatiety =  mongoTemplate.findOne(query , Variety.class);
        return vatiety;

    }

    @Override
    public PageResult<VarietyParams> findVarietyByType(String type ) {
//        MongoPageHelper mongoPageHelper=new MongoPageHelper();
//        Sort sort = new Sort(Sort.Direction.ASC, "DEVID").and(new Sort(Sort.Direction.ASC, "TIME"));//多条件DEVID、time

        final Query query = new Query(Criteria.where("type").is(type));
        query.with(new Sort(Sort.Direction.DESC, "update"));
        return mongoPageHelper.pageQuery(query, Variety.class, 14,
                1,variety->{
                VarietyParams varietyParams1=new VarietyParams();
                varietyParams1.setId(variety.getId());
                varietyParams1.setName(variety.getName());
                varietyParams1.setPicurl(variety.getPicurl());
                varietyParams1.setUpdate(variety.getUpdate());
                return varietyParams1;}
                , null);


    }

    @Override
    public int countAllVarietyNum() {
        Query query = new Query(Criteria.where("id").ne(null));
        return (int)mongoTemplate.count(query,Variety.class);
    }

    @Override
    public Variety findVarietyById(String id) {
        Query query=new Query(Criteria.where("id").is(id));
        Variety vatiety =  mongoTemplate.findOne(query , Variety.class);
        return vatiety;
    }

    @Override
    public PageResult<SearchVarietyparams> search(String all,int pageNum,int pageSize) {

        List<Variety>  vatiety=new ArrayList<Variety>();
//        Query query1 = new Query(Criteria.where("type").regex(all.trim()));
//        Query query2 = new Query(Criteria.where("name").regex(all.trim()));
        if ( mongoTemplate.find(new Query(Criteria.where("type").regex(all.trim())) , Variety.class).size()==0)
        {
            Query query = new Query(Criteria.where("name").regex(all.trim()));

//        vatiety =  mongoTemplate.find(query,Variety.class);
            return mongoPageHelper.pageQuery(query, Variety.class, pageSize,
                    pageNum,variety->{
                        SearchVarietyparams varietyParams1=new SearchVarietyparams();
                        varietyParams1.setId(variety.getId());
                        varietyParams1.setName(variety.getName());
                        varietyParams1.setPicurl(variety.getPicurl());
                        varietyParams1.setUpdate(variety.getUpdate());
                        varietyParams1.setArea(variety.getArea());
                        varietyParams1.setFromtv(variety.getFromtv());
                        varietyParams1.setContent(variety.getContent());
                        varietyParams1.setBtn(variety.getBtn());
                        varietyParams1.setType(variety.getType());
                        varietyParams1.setActor(variety.getActor());
                        return varietyParams1;
                    }
                    , null);
        }
        else if(mongoTemplate.find(new Query(Criteria.where("name").regex(all.trim())) , Variety.class).size()==0)
        {
            Query query = new Query(Criteria.where("type").regex(all.trim()));

//        vatiety =  mongoTemplate.find(query,Variety.class);
            return mongoPageHelper.pageQuery(query, Variety.class, pageSize,
                    pageNum,variety->{
                        SearchVarietyparams varietyParams1=new SearchVarietyparams();
                        varietyParams1.setId(variety.getId());
                        varietyParams1.setName(variety.getName());
                        varietyParams1.setPicurl(variety.getPicurl());
                        varietyParams1.setUpdate(variety.getUpdate());
                        varietyParams1.setArea(variety.getArea());
                        varietyParams1.setFromtv(variety.getFromtv());
                        varietyParams1.setContent(variety.getContent());
                        varietyParams1.setBtn(variety.getBtn());
                        varietyParams1.setType(variety.getType());
                        return varietyParams1;
                    }
                    , null);
        }
//        else if (mongoTemplate.find(new Query(Criteria.where("name").regex(all.trim())) , Variety.class).size()>0&&mongoTemplate.find(new Query(Criteria.where("type").regex(all.trim())) , Variety.class).size()>0)
//        {
//            Query query = new Query(Criteria.where("name").regex(all.trim()).orOperator(Criteria.where("type").regex(all.trim())));
//            Criteria criteria=new Criteria();
////            Criteria criteria1= Criteria.where("name").regex(all);
////            Criteria criteria2= Criteria.where("type").regex(all);
//            criteria.orOperator(Criteria.where("name").regex(all.trim()),Criteria.where("type").regex(all.trim()));
//            Query query = new Query(criteria);
//
//            return mongoPageHelper.pageQuery(query, Variety.class, pageSize,
//                    pageNum,variety->{
//                        SearchVarietyparams varietyParams1=new SearchVarietyparams();
//                        varietyParams1.setId(variety.getId());
//                        varietyParams1.setName(variety.getName());
//                        varietyParams1.setPicurl(variety.getPicurl());
//                        varietyParams1.setUpdate(variety.getUpdate());
//                        varietyParams1.setArea(variety.getArea());
//                        varietyParams1.setFromtv(variety.getFromtv());
//                        varietyParams1.setContent(variety.getContent());
//                        varietyParams1.setBtn(variety.getBtn());
//                        varietyParams1.setType(variety.getType());
//                        return varietyParams1;
//                    }
//                    , null);
//        }
        else{


            Criteria criteria=Criteria.where("name").regex(all.trim());

            Criteria criteria1=Criteria.where("type").regex(all.trim());
            Criteria criteria2=Criteria.where("name").ne("");

            Query query = new Query(criteria2.orOperator(criteria1,criteria));

            System.out.println(mongoTemplate.find(query,Variety.class).size());
            return mongoPageHelper.pageQuery(query, Variety.class, pageSize,
                    pageNum,variety->{
                        SearchVarietyparams varietyParams1=new SearchVarietyparams();
                        varietyParams1.setId(variety.getId());
                        varietyParams1.setName(variety.getName());
                        varietyParams1.setPicurl(variety.getPicurl());
                        varietyParams1.setUpdate(variety.getUpdate());
                        varietyParams1.setArea(variety.getArea());
                        varietyParams1.setFromtv(variety.getFromtv());
                        varietyParams1.setContent(variety.getContent());
                        varietyParams1.setBtn(variety.getBtn());
                        varietyParams1.setType(variety.getType());
                        return varietyParams1;
                    }
                    , null);
        }


//        Criteria c1= Criteria.where("name").regex(all);
//        Criteria c2=Criteria.where("actor").regex(all);
//        Criteria c3=Criteria.where("fromtv").regex(all);
//        Criteria c4=Criteria.where("type").regex(all);

//        Criteria cr = new Criteria();
//        cr.orOperator(c1,c2);



    }

    @Override
    public List<Variety> allVarietyList() {
        Query query=new Query(new Criteria());
        List<Variety> vatiety =  mongoTemplate.find(query,Variety.class);
        return vatiety;
    }

    @Override
    public PageResult findVarietyByTypeOrArea(String area, String type,int pageNum,int pageSize) {
        if(type.trim().equals("全部类型")&&!area.trim().equals("全部地区"))
        {
            final Query query = new Query(Criteria.where("area").is(area));
            query.with(new Sort(Sort.Direction.DESC, "update"));
            return mongoPageHelper.pageQuery(query, Variety.class, pageSize,
                    pageNum,variety->{
                        VarietyParams varietyParams1=new VarietyParams();
                        varietyParams1.setId(variety.getId());
                        varietyParams1.setName(variety.getName());
                        varietyParams1.setPicurl(variety.getPicurl());
                        varietyParams1.setUpdate(variety.getUpdate());
                        return varietyParams1;}
                    , null);
        }
        else if (!type.trim().equals("全部类型")&&area.trim().equals("全部地区"))
        {
            final Query query = new Query(Criteria.where("type").is(type));
            query.with(new Sort(Sort.Direction.DESC, "update"));
            return mongoPageHelper.pageQuery(query, Variety.class, pageSize,
                    pageNum,variety->{
                        VarietyParams varietyParams1=new VarietyParams();
                        varietyParams1.setId(variety.getId());
                        varietyParams1.setName(variety.getName());
                        varietyParams1.setPicurl(variety.getPicurl());
                        varietyParams1.setUpdate(variety.getUpdate());
                        return varietyParams1;}
                    , null);

        }
        else if(type.trim().equals("全部类型")&&area.trim().equals("全部地区"))
        {
            final Query query = new Query();
            query.with(new Sort(Sort.Direction.DESC, "update"));
            return mongoPageHelper.pageQuery(query, Variety.class, pageSize,
                    pageNum,variety->{
                        VarietyParams varietyParams1=new VarietyParams();
                        varietyParams1.setId(variety.getId());
                        varietyParams1.setName(variety.getName());
                        varietyParams1.setPicurl(variety.getPicurl());
                        varietyParams1.setUpdate(variety.getUpdate());
                        return varietyParams1;}
                    , null);
        }
        else{
            final Query query = new Query(Criteria.where("type").is(type).and("area").is(area));
            query.with(new Sort(Sort.Direction.DESC, "update"));
            return mongoPageHelper.pageQuery(query, Variety.class, pageSize,
                    pageNum,variety->{
                        VarietyParams varietyParams1=new VarietyParams();
                        varietyParams1.setId(variety.getId());
                        varietyParams1.setName(variety.getName());
                        varietyParams1.setPicurl(variety.getPicurl());
                        varietyParams1.setUpdate(variety.getUpdate());
                        return varietyParams1;}
                    , null);
        }


    }

    @Override
    public PageResult findVarietyByType(String type, String name) {

        final Query query = new Query(Criteria.where("type").is(type).and("name").ne(name));//ne不等于
        query.with(new Sort(Sort.Direction.DESC, "update"));
        return mongoPageHelper.pageQuery(query, Variety.class, 9,
                1,variety->{
                    VarietyParams varietyParams1=new VarietyParams();
                    varietyParams1.setId(variety.getId());
                    varietyParams1.setName(variety.getName());
                    varietyParams1.setPicurl(variety.getPicurl());
                    varietyParams1.setUpdate(variety.getUpdate());
                    return varietyParams1;}
                , null);
    }

    @Override
    public PageResult findVarietyByTypeforRecommend(String type) {
        final Query query = new Query(Criteria.where("type").is(type.trim()));
        query.with(new Sort(Sort.Direction.DESC, "update"));
        return mongoPageHelper.pageQuery(query, Variety.class, 9,
                1,variety->{
                    VarietyParams varietyParams1=new VarietyParams();
                    varietyParams1.setId(variety.getId());
                    varietyParams1.setName(variety.getName());
                    varietyParams1.setPicurl(variety.getPicurl());
                    varietyParams1.setUpdate(variety.getUpdate());
                    return varietyParams1;}
                , null);
    }
}


