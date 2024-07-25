$(document).ready(function() {
    function handleInputEvent(e) {
      var $this = $(this),
          label = $this.prev("label");

      if (e.type === "keyup") {
        label.toggleClass("active highlight", $this.val() !== "");
      } else if (e.type === "blur") {
        label.removeClass("highlight").toggleClass("active", $this.val() !== "");
      } else if (e.type === "focus") {
        label.toggleClass("highlight", $this.val() !== "");
      }
    }

    function handleTabClick(e) {
      e.preventDefault();

      var $this = $(this),
          target = $this.attr("href");

      $this.parent().addClass("active").siblings().removeClass("active");
      $(".tab-content > div").hide();
      $(target).fadeIn(600);
    }

    $(".form").find("input, textarea").on("keyup blur focus", handleInputEvent);
    $(".tab a").on("click", handleTabClick);

    // Inicializar la vista mostrando solo la pestaña activa
    $(".tab-content > div").hide();
    $(".tab-content > div.active").show();
    var initialTab = $(".tab.active a").attr("href");
    $(initialTab).show();
  });