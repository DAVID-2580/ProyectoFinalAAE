<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>ITL Budgets: Ingreso</title>

        <link rel="stylesheet" href="src/custom-alert/minfile/quantumalert.css" />
        <link rel="icon" type="image/png" href="favicon.ico?v=1"/>
        <link href="https://fonts.googleapis.com/css?family=IBM+Plex+Sans:400,600" rel="stylesheet">        
        <link rel="stylesheet" href="src/login/style.css">
        <link rel="stylesheet" href="src/landing/css/style.css">
        <link rel="stylesheet" href="src/css/animate.css">
        <script src="https://unpkg.com/animejs@3.0.1/lib/anime.min.js"></script>
        <script src="https://unpkg.com/scrollreveal@4.0.0/dist/scrollreveal.min.js"></script>
        <script src="src/custom-alert/minfile/quantumalert.js" defer></script>

        <style>                        
            .show {
                display: none;
            }

            #pop_heading, #pop_head_content {
                font-family: Arial, sans-serif;
                line-height: 40px;
            }

            .container {
                min-height: 450px;
            }

            .enterprise-radio {
                margin-top: 5px;
                margin-bottom: 0px;                
            }

            .enterprise-radio * {
                display: inline-block;
                vertical-align: middle;
                font-size: 20px;
            }

            .enterprise-radio input {
                width: 20px;                
            }            

            .sign-in-container input {
                margin-bottom: 15px;
            }

            .sign-up-container input {
                margin-bottom: 5px;
                height: 40px;
            }

            .credentials-title {
                color: #555;
            }

            .title {
                margin-bottom: 10px;
            }

            .show-sign-button {
                display: none;
            }

            @media screen and (max-width: 800px) {
                .hide {
                    display: none;
                }

                .show {
                    display: block;
                }
            }

            @media screen and (max-width: 700px) {
                .overlay-container {
                    display: none;
                }

                .show-sign-button {
                    display: inline-block;
                }

                .sign-in-container {
                    width: 100%;
                }

                .sign-up-container {
                    width: 100%;
                }

                .container.right-panel-active .sign-up-container {
                    transform: translateX(0%);
                }

                .container {
                    min-height: 600px;
                }
            }
        </style>
    </head>    

    <body>                
        <%
          String errorTitle = (String) request.getAttribute("errorTitle");
          String errorMessage = (String) request.getAttribute("errorMessage");

          if (errorTitle != null) {
        %>        
        <script>
            setTimeout(function () {
                Qual.errord('<%= errorTitle%>', '<%= errorMessage%>');
            }, 100);
        </script>        
        <%
          }

          request.removeAttribute("errorTitle");
          request.removeAttribute("errorMessage");
        %>
        
        <%
          String infoTitle = (String) request.getAttribute("infoTitle");
          String infoMessage = (String) request.getAttribute("infoMessage");

          if (infoTitle != null) {
        %>        
        <script>
            setTimeout(function () {
                Qual.success('<%= infoTitle %>', '<%= infoMessage %>');
            }, 100);
        </script>        
        <%
          }

          request.removeAttribute("infoTitle");
          request.removeAttribute("infoMessage");
        %>

        <div class="hide title animated fadeIn">
            <h2 style="display: inline-block; margin-top: 0">Ingreso: ITL Budgets</h2>
            <img style="display: inline-block" class="header-logo-image hide-logo" src="img/logo-2.png" alt="Logo" width="70px">
        </div>   

        <h2 class="show"></h2>

        <div class="container animated fadeIn" id="container">
            <div class="form-container sign-up-container">
                <form action="./register" method="POST">
                    <h1 class="credentials-title">Crear Cuenta</h1>                    
                    <span>Registrar una nueva cuenta</span>
                    <input type="email" name="email" placeholder="Correo electrónico" required/>
                    <input type="text" name="firstName" placeholder="Nombre(s)" required/>
                    <input type="text"  name="lastName" placeholder="Apellido(s)" required/>
                    <input type="password"  name="password" placeholder="Contraseña" required/>
                    <div class="enterprise-radio">
                        <div style="margin-right: 5px">
                            <input type="radio" name="is-enterprise-account" value="false" required/>
                            <span>Personal</span>
                        </div>
                        <div>
                            <input type="radio" name="is-enterprise-account" value="true" required/>
                            <span>Empresarial</span>
                        </div>                        
                    </div>                    
                    <a href="#" class="show-sign-button" id="signUpMovile">¿Ya tienes cuenta? 👈</a>
                    <input style="padding: 5px;" type="submit" class="button" value="Registrar" />
                </form>
            </div>
            <div class="form-container sign-in-container">
                <form action="./login" method="POST">
                    <h1 class="credentials-title">Iniciar Sesión</h1>
                    <div class="social-container">
                        <a href="#" class="social"><i class="fab fa-facebook-f"></i></a>
                        <a href="#" class="social"><i class="fab fa-google-plus-g"></i></a>
                        <a href="#" class="social"><i class="fab fa-linkedin-in"></i></a>
                    </div>
                    <span>Ingresar con una cuenta existente</span>
                    <input type="email" name="email" placeholder="Correo electrónico" />
                    <input type="password" name="password" placeholder="Contraseña" />
                    <a href="#" class="show-sign-button" id="signInMovile">¿No tienes cuenta? 🥺</a>
                    <input type="submit" class="button" value="Iniciar Sesión" />
                </form>
            </div>
            <div class="overlay-container">
                <div class="overlay">
                    <div class="overlay-panel overlay-left">
                        <h1>¿Ya tienes cuenta? 👈</h1>
                        <p>Inicia sesión con tu cuenta desde aqui</p>
                        <button class="ghost" id="signIn">Iniciar Sesión</button>
                    </div>
                    <div class="overlay-panel overlay-right">
                        <h1>¿No tienes cuenta? 🥺</h1>
                        <p>Regístrate ahora mismo y comienza a disfrutar de los beneficios de nuestra aplicación</p>
                        <button class="ghost" id="signUp">Registrarme</button>
                    </div>
                </div>
            </div>
        </div>

        <script src="src/login/main.js"></script>        
    </body>
</html>
