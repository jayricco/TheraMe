package com.therame.media;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
public class MediaRestController {

    @Autowired
    private MediaResolverService mediaResolverService;

    @RequestMapping("/watch")
    public String videoView(@RequestParam("v") String videoId, Model model) {
        model.addAttribute("videoId", videoId);
        return "watch";
    }

    @RequestMapping("/video")
    public void getVideo(HttpServletRequest request, HttpServletResponse response,
                         @RequestParam("path") String name) throws ServletException, IOException {
        request.setAttribute("video-request", new File("./media/" + name + ".mp4"));
        mediaResolverService.handleRequest(request, response);
    }

}
