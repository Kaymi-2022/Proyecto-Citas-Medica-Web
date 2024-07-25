// Set new default font family and font color to mimic Bootstrap's default styling
// Obteniendo el contenedor de datos
/*<![CDATA[*/
var valorDesdeThymeleaf = /*[[${labels}]]*/ "defaultValue";
console.log("Valor desde Thymeleaf:", valorDesdeThymeleaf);
/*]]>*/

// Pie Chart Example
var ctx = document.getElementById("myPieChart");
var myPieChart = new Chart(ctx, {
  type: "pie",
  data: {
    labels: ["Pendientes", "En Proceso", "Finalizadas"],
    datasets: [
      {
        data: [,2.3, 1.7,21.2],
        backgroundColor: ["#007bff", "#dc3545", "#ffc107", "#28a745"],
      },
    ],
  },
});
