package tads.eaj.ufrn.roupaapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tads.eaj.ufrn.roupaapp.model.Roupa;
import tads.eaj.ufrn.roupaapp.service.FileStorageService;
import tads.eaj.ufrn.roupaapp.service.RoupaService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@Controller
public class RoupaController {


    RoupaService service;
    FileStorageService fileStorageService;

    @Autowired
    public void setService(RoupaService service) {
        this.service = service;
    }

    @Autowired
    public void setFileStorageService(FileStorageService fileStorageService){
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String getPageAdmin(Model model){
        var listaRoupas =  service.findAll();
        model.addAttribute("listaRoupas", listaRoupas);
        return "admin";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getPageHome(Model model, HttpServletRequest request, HttpServletResponse response, @ModelAttribute("msg") String msg){
        var listaRoupas =  service.findAll();
        model.addAttribute("listaRoupas", listaRoupas);
        model.addAttribute("msg", msg);

        HttpSession sessao = request.getSession();

        SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy--hh:mm:ss");
        Cookie c = new Cookie("acesso", formatoData.format(new Date(sessao.getCreationTime())));
        c.setMaxAge(60*60*24);
        response.addCookie(c);


        if(sessao.getAttribute("carrinho")!=null){
            ArrayList<Roupa> r = (ArrayList<Roupa>) sessao.getAttribute("carrinho");
            model.addAttribute("carrinho", r.size());
        }
        else{
            model.addAttribute("carrinho", 0);
        }

        return "index";
    }

    @RequestMapping(value = "/adicionarCarrinho/{id}", method = RequestMethod.GET)
    public String getAdicionarCarrinho(@PathVariable(name = "id") Long id, HttpServletRequest request, RedirectAttributes attributes){
        HttpSession session = request.getSession();
        if(session.getAttribute("carrinho") == null){
            session.setAttribute("carrinho", new ArrayList<Roupa>());
        }
        Roupa roupa = service.findById(id);
        System.out.println(service.findById(id));
        ArrayList<Roupa> lista = (ArrayList<Roupa>)session.getAttribute("carrinho");

        lista.add(roupa);
        attributes.addAttribute("msg", "Roupa adicionada no carrinho.");
        return "redirect:/";
    }

    @RequestMapping(value = "/verCarrinho", method = RequestMethod.GET)
    public String getVerCarrinho(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();

        System.out.println(service.findAll());

        if(session.getAttribute("carrinho") == null) {
            return "redirect:/";
        }else{
            ModelAndView mav = new ModelAndView("carrinho");
            ArrayList<Roupa> lista = (ArrayList<Roupa>) session.getAttribute("carrinho");
            model.addAttribute("listaRoupas", lista);
            System.out.println(lista);
            return "carrinho";
        }
    }

    @RequestMapping(value = "/finalizarCarrinho", method = RequestMethod.GET)
    public String getFinalizarCarrinho(HttpServletRequest request){
        request.getSession().invalidate();
        return "redirect:/";
    }


    @RequestMapping(value = "/cadastrar", method = RequestMethod.GET )
    public String getFormCadastro(Model model){
        Roupa roupa = new Roupa();
        model.addAttribute("roupa", roupa);
        return "cadastrar";
    }

    @RequestMapping(value = "/salvar", method = RequestMethod.POST)
    public String doSalvar(@ModelAttribute @Valid Roupa roupa, Errors errors, @RequestParam("file") MultipartFile file, RedirectAttributes attributes) {
        if (errors.hasErrors()) {
            return "cadastrar";
        } else {
            if(roupa.getId()==null){
                attributes.addAttribute("msg", "Cadastrado com sucesso");
            }
            else{
                attributes.addAttribute("msg", "Editado com sucesso");
            }
            if(file.isEmpty()){
                roupa.setImageURI(service.findById(roupa.getId()).getImageURI());
            }
            else{
                Date data = Calendar.getInstance().getTime();
                String filename = data + file.getOriginalFilename();
                roupa.setImageURI(filename);
                fileStorageService.save(file, filename);
            }
            service.save(roupa);
            return "redirect:/";
        }


    }
    @RequestMapping(value = "/deletar/{id}")
    public String doDeletar(@PathVariable(name="id") Long id){
        Roupa roupa = service.findById(id);
        roupa.setExcluido(new Date());
        service.save(roupa);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/editar/{id}")
    public ModelAndView getFormEdicao(@PathVariable(name = "id") Long id){
        ModelAndView modelAndView = new ModelAndView("editar");
        Roupa roupa = service.findById(id);
        modelAndView.addObject("roupa", roupa);
        return modelAndView;
    }


}
