<?php  
	
	copy($_FILES['image']['tmp_name'], $_FILES['image']['name']);
	echo "Archivo subido correctamente";

	$nombre = $_FILES['image']['name'];
	echo "<img src=\"$nombre\">";

?>