$(function () {
  $(document).tooltip({
    content: function () {
      return $(this).prop('title');
       }
  });
});
