<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>        
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Two Factor Challenge</title>

        <link rel="stylesheet" href="src/custom-alert/minfile/quantumalert.css" />
        <link rel="icon" type="image/png" href="favicon.ico?v=1"/>
        <link href="https://fonts.googleapis.com/css?family=IBM+Plex+Sans:400,600" rel="stylesheet">        
        <link rel="stylesheet" href="src/two-factor/style.css">
        <link rel="stylesheet" href="src/css/animate.css">
        <link rel="stylesheet" href="src/custom-alert/minfile/quantumalert.css" />
        
        <script src="src/custom-alert/minfile/quantumalert.js" defer></script>
        
        <style>
            @media screen and (max-width: 550px) {
                .login {
                    width: 200px;
                }
            }
        </style>
    </head>
    <body class="animated fadeIn">
        <%
          String errorTitle = (String) request.getAttribute("errorTitle");
          String errorMessage = (String) request.getAttribute("errorMessage");
          
          if(errorTitle != null) {
        %>        
        <script>
            setTimeout(function () {
                Qual.errord('<%= errorTitle %>', '<%= errorMessage %>');
            }, 100);            
        </script>        
        <%
          }
        %>

        <%                             
          String email = (String) session.getAttribute("email");
        %>
        
        <form class="login" action="./two-factor" method="POST">
            <p style="color: #555; font-family: Arial, sans-serif; font-weight: bold; font-size: 20px">
                <%= email %>
            </p>
            <input type="text" maxlength="6" name="code" placeholder="TOTP Code">
            <button>Enviar</button>
        </form>
    </body>
</html>
