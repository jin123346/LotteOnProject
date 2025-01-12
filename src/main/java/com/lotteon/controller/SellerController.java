package com.lotteon.controller;



import com.lotteon.dto.product.*;
import com.lotteon.service.UserService;
import com.lotteon.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
@Controller
@RequestMapping("/seller")
public class SellerController {


    private final ProductService productService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager; // AuthenticationManager로 수정
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/product/list")
    public String productList(Model model, PageRequestDTO pageRequestDTO,Authentication authentication) {

        String user = authentication.getName();
        ProductPageResponseDTO productPageResponseDTO =  productService.selectProductsBySellerId(user,pageRequestDTO);
        log.info("파일~~!!!!!!!!!!!!!!!!"+productPageResponseDTO.getProductFileDTOList());

        model.addAttribute("productPageResponseDTO", productPageResponseDTO);
        log.info(productPageResponseDTO);

        return "content/admin/product/admin_productlist"; // Points to the "content/sellerDynamic" template for product listing
    }

    @GetMapping("/product/register")
    public String productRegister(Model model) {
        return "content/admin/product/admin_productReg"; // Points to the "content/sellerDynamic" template for product registration
    }



    @PostMapping("/product/register")
    public String insertProduct(@ModelAttribute ProductRequestDTO productRequestDTO, Authentication auth, Model model) {
        log.info("전달은 된다.");
        log.info("파일~~!!!!!!!!!!!!!!!!"+productRequestDTO.getFiles());

        //product insert
        ProductResponseDTO responseDTO = new ProductResponseDTO(productRequestDTO);
        log.info("auth ~~~~~~~~~~~~~~~~~~"+auth.getName());
        log.info("responseDTO");

        long result= productService.insertProduct(responseDTO);
        //option insert
        log.info("insertProduct");
        if(result >0){
            return "redirect:/seller/product/list";

        }else{
            return "redirect:/seller/product/register?success=200";
        }
    }


    @GetMapping("/product/delete")
    public String productDelete(@RequestParam("id") long id,Model model, Authentication authentication) {
        String user = authentication.getName();
        log.info(id);
        int result = productService.deleteProduct(id);
        if(result >0){
            //성공시
            return "redirect:/seller/product/list?success=200";
        }

        //실패시
        return "redirect:/seller/product/register?success=100";
    }

    @GetMapping("/order/delivery")
    public String deliveryStatus(Model model) {
        model.addAttribute("content", "delivery");
        return "content/admin/order/admin_Delivery"; // Points to the "content/sellerDynamic" template for delivery orders
    }

    @GetMapping("/order/status")
    public String orderStatus(Model model) {
        model.addAttribute("content", "status");
        return "content/admin/order/admin_Order"; // Points to the "content/sellerDynamic" template for order status
    }



    @GetMapping("/coupon/list")
    public String couponList(Model model) {
        model.addAttribute("cate", "coupon");
        return "content/admin/coupon/list"; // Points to the "content/sellerDynamic" template for coupon management
    }

    @GetMapping("/coupon/issued")
    public String couponIssued(Model model) {
        model.addAttribute("cate", "coupon");
        model.addAttribute("content", "issued");
        return "content/admin/coupon/issued"; // Points to "content/admin/coupon/issued"
    }

    @GetMapping("/login")
    public String sellerLogin(Model model) {

        return "content/admin/adminLogin";
    }


    @PostMapping("/login")
    public String login(@RequestParam("inId") String username,
                        @RequestParam("Password") String password,
                        Model model) {

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(username, password);
        try {
            Authentication authentication = authenticationManager.authenticate(authToken); // 인증 처리
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 성공 시 SecurityContext에 설정
        } catch (AuthenticationException e) {
            log.error("Authentication failed: " + e.getMessage());
            return "redirect:/user/login?error=true"; // 인증 실패 시 처리
        }
        // 로그인 성공 시 Member의 name 값을 가져와 모델에 추가
        String memberName = userService.getMemberNameByUsername(username);
        log.info("memberName:"+memberName);
        model.addAttribute("memberName", memberName);

        return "redirect:/admin/main"; // 로그인 성공 후 이동할 페이지
    }


}
