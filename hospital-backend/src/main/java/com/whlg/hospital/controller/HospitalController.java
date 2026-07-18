package com.whlg.hospital.controller;

import com.whlg.hospital.entity.Hospital;
import com.whlg.hospital.service.HospitalService;
import com.whlg.hospital.util.R;
import com.whlg.hospital.vo.HospitalVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.whlg.hospital.vo.DepartmentTreeVo;
import java.util.List;

/**
 * 鍖婚櫌鎺у埗鍣? */

@RestController
@RequestMapping("/hospital")
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    /**
     * 鑾峰彇鍖婚櫌鍒楄〃锛堟敮鎸佸垎椤靛拰绉戝绛涢€夛級
     * @param departmentId 绉戝ID锛堝彲閫夛級
     * @param page 椤电爜
     * @param pageSize 姣忛〉鏁伴噺
     * @return 鍖婚櫌鍒楄〃
     */
    @GetMapping("/list")
    public R<List<HospitalVo>> listHospitals(
            @RequestParam(required = false) Long departmentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        List<HospitalVo> hospitals = hospitalService.listHospitals(departmentId, page, pageSize);
        return R.createSuccess(hospitals);
    }

    /**
     * 鑾峰彇鍖婚櫌鎬绘暟
     * @param departmentId 绉戝ID锛堝彲閫夛級
     * @return 鍖婚櫌鎬绘暟
     */
    @GetMapping("/count")
    public R<Integer> countHospitals(@RequestParam(required = false) Long departmentId) {
        int count = hospitalService.countHospitals(departmentId);
        return R.createSuccess(count);
    }

    /**
     * 鏍规嵁ID鑾峰彇鍖婚櫌璇︽儏
     * @param id 鍖婚櫌ID
     * @return 鍖婚櫌璇︽儏
     */
    @GetMapping("/detail/{id}")
    public R<HospitalVo> getHospitalDetail(@PathVariable Long id) {
        HospitalVo hospital = hospitalService.getHospitalDetail(id);
        return R.createSuccess(hospital);
    }

    /**
     * 鑾峰彇鎺ㄨ崘鍖婚櫌鍒楄〃
     * @param limit 鏁伴噺闄愬埗
     * @return 鎺ㄨ崘鍖婚櫌鍒楄〃
     */
    @GetMapping("/listTop")
    public R<List<HospitalVo>> listTopHospitals(@RequestParam(defaultValue = "4") int limit) {
        List<HospitalVo> hospitals = hospitalService.listTopHospitals(limit);
        return R.createSuccess(hospitals);
    }

    /**
     * 鏍规嵁鍖婚櫌ID鑾峰彇鍖婚櫌鐨勭瀹ゅ垪琛?     * @param hospitalId 鍖婚櫌ID
     * @return 绉戝鍒楄〃
     */
    @GetMapping("/departments/{hospitalId}")
    public R<List<Long>> getHospitalDepartments(@PathVariable Long hospitalId) {
        List<Long> departmentIds = hospitalService.getHospitalDepartmentIds(hospitalId);
        return R.createSuccess(departmentIds);
    }

    /**
     * 鑾峰彇鍖婚櫌鐨勭瀹ゆ爲褰㈢粨鏋?     * @param hospitalId 鍖婚櫌ID
     * @return 绉戝鏍戝舰缁撴瀯
     */
    @GetMapping("/departments/tree/{hospitalId}")
    public R<List<DepartmentTreeVo>> getHospitalDepartmentTree(@PathVariable Long hospitalId) {
        List<DepartmentTreeVo> tree = hospitalService.getHospitalDepartmentTree(hospitalId);
        return R.createSuccess(tree);
    }
}

