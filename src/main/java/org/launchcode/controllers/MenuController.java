package org.launchcode.controllers;

import org.launchcode.forms.AddMenuItemForm;
import org.launchcode.models.Cheese;
import org.launchcode.models.Menu;
import org.launchcode.models.data.CheeseDao;
import org.launchcode.models.data.MenuDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Created by cmp on 7/21/2017.
 */

@Controller
@RequestMapping(value = "menu")
public class MenuController {

    @Autowired
    private CheeseDao cheeseDao;

    @Autowired
    private MenuDao menuDao;


    @RequestMapping(value = "")
    public String index(Model model) {

        model.addAttribute("menus", menuDao.findAll());
        model.addAttribute("title", "Menus");
        return "menu/index";
    }

    @RequestMapping(value="add", method = RequestMethod.GET)
    public String add(Model model){

        model.addAttribute("title", "Add Menus");
        model.addAttribute(new Menu());
        model.addAttribute("menus", menuDao.findAll());
        return "menu/add";
    }

    @RequestMapping(value="add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid Menu newMenu, Errors errors,
                      @RequestParam int menuId, Model model){

        if (errors.hasErrors()) {
            model.addAttribute("title", "Add Menu");
            return "menu/add";
        }

        menuDao.save(newMenu);

        return "redirect:view/" + newMenu.getId();
    }


    @RequestMapping(value = "view/{menuId}", method = RequestMethod.GET)
    public String addItem(Model model, @PathVariable int menuId){

        Menu aMenu = menuDao.findOne(menuId);

        AddMenuItemForm form = new AddMenuItemForm(cheeseDao.findAll(), aMenu);

        model.addAttribute("form", form);
        model.addAttribute("title", "Add item to menu" + aMenu.getName());

        return "menu/add-item";
    }
    @RequestMapping(value="view/{menuId}", method = RequestMethod.POST)
    public String addItem(@ModelAttribute @Valid AddMenuItemForm form, Model model,
                          Errors errors){
        if(errors.hasErrors()){
            return "menu/add-item";
        }

        Cheese aCheese = cheeseDao.findOne(form.getCheeseId());
        Menu aMenu = menuDao.findOne(form.getMenuId());
        aMenu.addItem(aCheese);
        menuDao.save(aMenu);

        return "redirect:/menu/view/" + aMenu.getId();

    }

}
