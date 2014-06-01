function showDate(inputYear, inputMonth, inputDay) {
           // Günler
           var weekday = new Array(7);
           weekday[1] = 'Pazar';
           weekday[2] = 'Pazartesi';
           weekday[3] = 'Salı';
           weekday[4] = 'Çarşamba';
           weekday[5] = 'Perşembe';
           weekday[6] = 'Cuma';
           weekday[7] = 'Cumartesi';
           // Aylar
           var month = new Array(12);
           month[1] = 'Ocak';
           month[2] = 'Şubat';
           month[3] = 'Mart';
           month[4] = 'Nisan';
           month[5] = 'Mayıs';
           month[6] = 'Haziran';
           month[7] = 'Temmuz';
           month[8] = 'Ağustos';
           month[9] = 'Eylül';
           month[10] = 'Ekim';
           month[11] = 'Kasım';
           month[12] = 'Aralık';
 
           var today = new Date();
 
   yr_st = " 19";
   yr = today.getYear();
   if ( yr > 99 )
   {
   yr_st =" ";
   if ( yr < 2000 ) yr += 1900;
   }
   return inputDay + ' ' + month[inputMonth+1] +yr_st+ yr ;
}