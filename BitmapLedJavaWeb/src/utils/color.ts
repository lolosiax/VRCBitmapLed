export function HSVtoRGB(h, s, v) {
  let i, f, p1, p2, p3;
  let r = 0, g = 0, b = 0;
  if (s < 0) s = 0;
  if (s > 1) s = 1;
  if (v < 0) v = 0;
  if (v > 1) v = 1;
  h %= 360;
  if (h < 0) h += 360;
  h /= 60;
  i = Math.floor(h);
  f = h - i;
  p1 = v * (1 - s);
  p2 = v * (1 - s * f);
  p3 = v * (1 - s * (1 - f));
  switch(i) {
    case 0: r = v;  g = p3; b = p1; break;
    case 1: r = p2; g = v;  b = p1; break;
    case 2: r = p1; g = v;  b = p3; break;
    case 3: r = p1; g = p2; b = v;  break;
    case 4: r = p3; g = p1; b = v;  break;
    case 5: r = v;  g = p1; b = p2; break;
  }
  return [Math.round(r * 255), Math.round(g * 255), Math.round(b * 255)];
}
