<!DOCTYPE html>

<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link rel="icon"  href="../icon.ico" />
        <title>Wolosky Contacto</title>
        
        <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
        <link type="text/css" rel="stylesheet" href="materialize/css/materialize.min.css"  media="screen,projection"/>
        <link href="css/styles.css" type="text/css" rel="stylesheet" media="screen,projection"/>
        <link rel="stylesheet" type="text/css" href="sweet/dist/sweetalert.css">
        
        <script type="text/javascript" src="js/jquery-2.1.1.min.js"></script>
        <script type="text/javascript" src="materialize/js/materialize.min.js"></script>
        <script src="sweet/dist/sweetalert.min.js"></script> 
        <script src="js/init.js"></script>
    </head>
    <body>
        <div class="container">
            <h1 class="center-align">Contáctanos</h1>
            <div class="row">
                <div class="col l10 m8 s12 offset-l1 offset-m2">
                    <div class="card">
                        <div class="card-content">
                            <span class="card-title" style="font-weight: bold;">Envía tus datos!</span>
                            <form id="w-form">
                                <div class="row">
                                    <div class="input-field col l6 m6 s12">
                                        <i class="material-icons prefix">perm_identity</i>
                                        <input placeholder="Introduce tu nombre" id="first_name" required="required" type="text" class="validate">
                                        <label for="first_name">Nombre</label>
                                    </div>
                                    <div class="input-field col l6 m6 s12">
                                        <input id="last_name" required="required" type="text" class="validate">
                                        <label for="last_name">Apellidos</label>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="input-field col s12">
                                        <i class="material-icons prefix">email</i>
                                        <input id="email" required="required" type="email" class="validate">
                                        <label for="email" data-error="Introducir e-mail válido" data-success="Correcto! :D">Email</label>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="input-field col s12">
                                        <i class="material-icons prefix">perm_identity</i>
                                        <select id="gender">
                                            <option disabled selected value>-- Seleccionar género --</option>
                                            <option value="male">Masculino</option>
                                            <option value="female">Femenino</option>
                                        </select>
                                        <label>Sexo</label>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="input-field col l4 s12">
                                        <i class="material-icons prefix">phone</i>
                                        <input id="cellphone" type="text" required="required" pattern="[\d]{10}" title="Introduzca un celular válido" maxlength="10" length="10">
                                        <label for="cellphone">Celular</label>
                                    </div>
                                    <div class="input-field col l4 s12">
                                        <i class="material-icons prefix">perm_identity</i>
                                        <select id="years">
                                            <option disabled selected value>-- Elegir edad --</option>
                                        </select>
                                        <label>Edad</label>
                                    </div>
                                    <div class="input-field col l4 s12">
                                        <i class="material-icons prefix">info</i>
                                        <select id="type">
                                            <option disabled selected value>-- Seleccionar un tipo --</option>
                                            <option>Estudiante</option>
                                            <option>Padre de familia</option>
                                        </select>
                                        <label>Tipo</label>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="input-field col s12">
                                        <textarea id="messageTA" class="materialize-textarea"></textarea>
                                        <label for="messageTA">Mensaje:</label>
                                    </div>
                                </div>
                                <div class="right-align">
                                    <button class="btn waves-effect waves-light" type="submit" name="action">Enviar
                                        <i class="material-icons right">send</i>
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>