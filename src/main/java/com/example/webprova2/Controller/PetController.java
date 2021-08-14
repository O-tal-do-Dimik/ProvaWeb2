package com.example.webprova2.Controller;


import com.example.webprova2.Model.Pet;
import com.example.webprova2.Service.FileStorageService;
import com.example.webprova2.Service.PetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;


@Controller

public class PetController {

    PetService service;
    FileStorageService fileStorageService;

    @Autowired
    public void setFileStorageService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

   @Autowired
    public void setService(PetService service) {
        this.service = service;
    }

    @RequestMapping(value = {"/", "/pet"},method =  RequestMethod.GET)
    public String getPage(Model model){
        var list = service.findAll();
        model.addAttribute("listarMercadorias",list);
        return "index";
    }

    @RequestMapping("/cadastro")
    public String getFromCadastro(Model model){
        Pet pet = new Pet();
        model.addAttribute("pet", pet);
        return "cadastro";
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public String doSalvar(@ModelAttribute @Valid Pet pet, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes){
        if (errors.hasErrors()){
            return "cadastro";
        }else{
            pet.setImagemUri(file.getOriginalFilename());
            service.save(pet);
            fileStorageService.save(file);

            redirectAttributes.addAttribute("msg", "Cadastro realizado com sucesso");
            return "redirect:/admin";

        }
    }

    @RequestMapping("/deletar/{id}")
    public String doDelete(@PathVariable(name = "id") Long id){
        service.delete(id);
        return "redirect:/";
    }

    @RequestMapping("/editar/{id}")
    public ModelAndView getFormEdicao(@PathVariable(name = "id") Long id){
        ModelAndView modelAndView = new ModelAndView ("edicao");
        Pet pet  = service.findById(id);
        modelAndView.addObject("pet", pet);
        return modelAndView;
    }

    @RequestMapping("/adicionarCarrinho/{id}")
    public String adCarrinho(HttpServletRequest request,RedirectAttributes redirectAttributes,@PathVariable(name = "id") Long id) throws IOException, ServletException {
        HttpSession session =request.getSession();
        var cont=0;
        if(session.getAttribute("carrinho") ==null){
            session.setAttribute("carrinho", new ArrayList<Pet>());
        }
        Pet pet =service.findById(id);
        ArrayList<Pet> carrinho1 =(ArrayList<Pet>)session.getAttribute("carrinho");
        carrinho1.add(pet);
        cont++;
        redirectAttributes.addAttribute("msg","O Jogo adicionado no carrinho foi:"+pet.getNome());
        redirectAttributes.addAttribute("cont", cont);
        return "redirect:/";

    }

    @RequestMapping("/vercarrinho")
    public ModelAndView getCarrinho(HttpServletRequest request, Model model){
        HttpSession session= request.getSession();
        if(session.getAttribute("carrinho")==null){
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            return modelAndView;
        }else{
            ModelAndView modelAndView = new ModelAndView("carrinho");
            modelAndView.addObject("carrinho",session.getAttribute("carrinho"));
            return modelAndView;
        }

    }

    @RequestMapping(value = {"/admin" }, method = RequestMethod.GET)
    public String listaex(Model model){
        var listpet = service.findAll();
        model.addAttribute("petls", listpet);
        return "admin";
    }


    @RequestMapping(value = "/finalizarcompras")
    public String finishCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }


}
