package com.feicheng.blog.controller;

import com.feicheng.blog.common.MenuTree;
import com.feicheng.blog.common.PageResult;
import com.feicheng.blog.common.ResponseResult;
import com.feicheng.blog.entity.Menu;
import com.feicheng.blog.service.MenuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 菜单管理Controller
 *
 * @author Lenovo
 */
@Controller
@RequestMapping("menu")
public class MenuController {


    @Autowired
    private MenuService menuService;


    /**
     * 获取菜单树
     *
     * @return
     */
    @GetMapping("select")
    @RequiresPermissions("menu:view")
    public ResponseEntity<PageResult<Menu>> selectMenuTree() {

        PageResult<Menu> pageResult = this.menuService.selectMenus();

        if (CollectionUtils.isEmpty(pageResult.getData())) {

            return ResponseEntity.ok(pageResult);
        }

        return ResponseEntity.ok(pageResult);
    }


    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @PostMapping("edit")
    @RequiresPermissions("menu:edit")
    public ResponseEntity<ResponseResult> editMenu(Menu menu) {

        Map<String, Object> map = this.menuService.editMenu(menu);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }
        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }


    /**
     * 添加菜单
     *
     * @param menu
     * @return
     */
    @PostMapping("add")
    @RequiresPermissions("menu:add")
    public ResponseEntity<ResponseResult> addMenu(Menu menu) {


        Map<String, Object> map = this.menuService.addMenu(menu);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }
        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }


    /**
     * 删除菜单
     *
     * @param authorityId
     * @param isMenu
     * @return
     */
    @PostMapping("delete")
    @RequiresPermissions("menu:delete")
    public ResponseEntity<ResponseResult> deleteMenu(@RequestParam("authorityId") Long authorityId,
                                                     @RequestParam("isMenu") Integer isMenu) {

        Map<String, Object> map = this.menuService.delete(authorityId, isMenu);

        if (StringUtils.equals(map.get("message").toString(), "error")) {

            return ResponseEntity.ok(new ResponseResult(400, map.get("result").toString()));
        }
        return ResponseEntity.ok(new ResponseResult(200, map.get("result").toString()));
    }


    /**
     * 获取节点树所有的数据
     *
     * @return
     */
    @GetMapping("tree")
    public ResponseEntity<List<MenuTree<Menu>>> selectedNode() {

        MenuTree<Menu> menus = this.menuService.selectedNodes();

        if (CollectionUtils.isEmpty(menus.getChildren())) {

            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(menus.getChildren());
    }
}
