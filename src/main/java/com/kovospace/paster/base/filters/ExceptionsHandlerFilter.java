package com.kovospace.paster.base.filters;

//@Component
//@Order(1)
public class ExceptionsHandlerFilter /*extends OncePerRequestFilter*/ {

//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public ExceptionsHandlerFilter(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }
//
//    @Override
//    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//    throws ServletException, IOException
//    {
//        System.out.println("exceptions catching filter called");
//
//        try {
//            filterChain.doFilter(request, response);
//        } catch (Exception e) {
//            HttpStatus httpStatus;
//            switch (e.getMessage()) {
//                case "general.endpoint.authentication.apikey.wrong":
//                case "general.endpoint.authentication.apikey.missing":
//                    httpStatus = HttpStatus.UNAUTHORIZED;
//                    break;
//                default:
//                    httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//            }
//            ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(e.getMessage());
//            response.setStatus(httpStatus.value());
//            response.getWriter().write(convertObjectToJson(errorResponseDTO));
//        }
//    }
//
//    public String convertObjectToJson(Object object) throws JsonProcessingException {
//        if (object == null) {
//            return null;
//        }
//        return objectMapper.writeValueAsString(object);
//    }
}
