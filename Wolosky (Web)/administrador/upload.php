<!DOCTYPE html>
<html>
<head>
	<title>Guardar imagen</title>
</head>
<body>
	<h1>Subir un archivo! :D</h1>
	<form action="save.php" method="post" enctype="multipart/form-data">
		<label for="imageInput">Elegir imagen:</label><br>
		<input id="imageInput" type="file" name="image"/><br>
		<input type="submit" value="Upload" />
	</form>
</body>
</html>